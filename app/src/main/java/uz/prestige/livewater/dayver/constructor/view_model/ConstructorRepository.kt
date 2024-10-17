package uz.prestige.livewater.dayver.constructor.view_model

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.dayver.constructor.type.DeviceType
import uz.prestige.livewater.dayver.constructor.type.RegionType
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.convertDayverDeviceSecondaryToDeviceType
import uz.prestige.livewater.utils.convertDayverRegionSecondaryToRegionType
import uz.prestige.livewater.utils.toFormattedDate
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ConstructorRepository @Inject constructor(
    private val apiService: ApiServiceDayver,
    private val context: Context
) {
    private var regionList = listOf<RegionType>()
    private var devicesList = listOf<DeviceType>()
    private var url = "https://back1.livewater.uz/basedata/xlsx?"

    suspend fun getRegions() = flow {
        val response = apiService.getDayverRegions()
        regionList = response.body()!!.convertDayverRegionSecondaryToRegionType()
        emit(regionList)
    }

    suspend fun getConstructorData(
        startTime: String?,
        endTime: String?,
        regionId: String?,
        deviceId: String?
    ) {
        val queryMap = mutableMapOf<String, String>()
        startTime?.let { queryMap["filter[start]"] = it }
        endTime?.let { queryMap["filter[end]"] = it }
        regionId?.let { queryMap["filter[region]"] = it }
        deviceId?.let { queryMap["filter[device]"] = it }

        val response = apiService.getBaseData(queryMap)

        Log.d("ConstructorRepository", "getConstructorData: ${response.body()}")
    }

    suspend fun getDevicesSerialByRegion() = flow {
        val response =
            apiService.getDayverDevices(limit = 100, 0)
        devicesList = response.body()!!.convertDayverDeviceSecondaryToDeviceType()
        emit((devicesList))
    }

    fun getDevicesByRegionId(regionId: String?) = flow {
        val list = devicesList.filter { it.regionId == regionId }
        emit(list)
    }

    fun getDeviceIdBySerialNumber(deviceSerial: String?) =
        devicesList.find { it.serialNumber == deviceSerial }?.id

    fun generateFilterUrl(
        startTime: String?,
        endTime: String?,
        regionId: String?,
        deviceId: String?
    ) {
        url = ""
        val baseUrl = "https://back2.livewater.uz/basedata/xlsx?"
        Log.d("ConstructorRepository", "generateFilterUrl: $startTime $endTime $regionId $deviceId")
        val startEndTime =
            if (startTime.isNullOrEmpty() || endTime.isNullOrEmpty()) "&filter[start]=946666800000&filter[end]=1893438000000" else "&filter[start]=$startTime&filter[end]=$endTime"
        val region = if (regionId.isNullOrEmpty()) "&filter[region]=$regionId" else ""
        val device = if (deviceId.isNullOrEmpty()) "&filter[device]=$deviceId" else ""

        url += baseUrl + startEndTime + region + device
    }

    fun downloadFileToInternalStorage() {
        Log.d("ConstructorRepository", "downloadFileToInternalStorage: $url")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val fileName = "Livewater ${System.currentTimeMillis().toFormattedDate()}.xlsx"

        // Create the request
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Livewater file: $fileName")
            .addRequestHeader("Authorization", "Bearer ${TokenManager.getToken(context)}")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            ) // Temporarily save in Downloads directory

        // Start the download
        val downloadId = downloadManager.enqueue(request)

        // Register a BroadcastReceiver to listen for download completion
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                // Check if the completed download is the one we're interested in
                if (downloadId == id) {
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor: Cursor = downloadManager.query(query)
                    try {
                        if (cursor.moveToFirst()) {
                            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            if (statusIndex != -1) {
                                val status = cursor.getInt(statusIndex)
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    // Download was successful
                                    val fileUri =
                                        downloadManager.getUriForDownloadedFile(downloadId)
                                    fileUri?.let {
                                        // Move file to internal storage
                                        saveFileToInternalStorage(context, it, fileName)
                                        Toast.makeText(
                                            context,
                                            "Download complete!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    // Handle other statuses (optional)
                                    val reasonIndex =
                                        cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                                    val reason =
                                        if (reasonIndex != -1) cursor.getInt(reasonIndex) else 0
                                    Log.e("DownloadError", "Download failed. Reason: $reason")
                                }
                            } else {
                                Log.e("DownloadError", "COLUMN_STATUS not found in the cursor")
                            }
                        }
                    } finally {
                        cursor.close()
                    }
                    // Unregister the receiver after handling the download
                    context.unregisterReceiver(this)
                }
            }
        }

        // Register the receiver to listen for download completion
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun saveFileToInternalStorage(context: Context, fileUri: Uri, fileName: String) {
        try {
            // Open the input stream from the downloaded file
            val inputStream = context.contentResolver.openInputStream(fileUri)
            // Create the output file in the app's internal storage
            val outputFile = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(outputFile)

            // Copy the file from external to internal storage
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Log.d("DownloadManager", "File saved to internal storage: ${outputFile.absolutePath}")
        } catch (e: Exception) {
            Log.e("DownloadError", "Error saving file to internal storage", e)
        }
    }
}
