package uz.prestige.livewater.utils

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.documentfile.provider.DocumentFile
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import uz.prestige.livewater.constructor.type.ConstructorType
import uz.prestige.livewater.constructor.type.DeviceType
import uz.prestige.livewater.constructor.type.RegionType
import uz.prestige.livewater.constructor.type.secondary.DeviceSecondaryType
import uz.prestige.livewater.constructor.type.secondary.RegionSecondaryType
import uz.prestige.livewater.constructor.type.secondary.SecondaryConstructor
import uz.prestige.livewater.device.type.OwnerType
import uz.prestige.livewater.device.type.secondary_type.OwnerSecondaryType
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Long.toFormattedTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this)
    return dateFormat.format(date)
}

fun Long.toFormattedDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this)
    return dateFormat.format(date)
}

fun DeviceSecondaryType.convertDeviceSecondaryToDeviceType(): List<DeviceType> {
    var counter = 1
    return this@convertDeviceSecondaryToDeviceType.data.map { data ->
        DeviceType(
            id = data._id,
            numbering = counter,
            serialNumber = data.serie,
            regionId = data.region._id,
            regionName = data.region.name,
            devicePrivateKey = data.device_privet_key,
            long = data.long.toString(),
            lat = data.lat.toString(),
            ownerName = data.owner.first_name + " " + data.owner.last_name,
            createdAt = data.created_at,
            objectName = data.name
        ).also { counter++ }
    }
}

fun RegionSecondaryType.convertRegionSecondaryToRegionType(): List<RegionType> {
    return this@convertRegionSecondaryToRegionType.data.map { data ->
        RegionType(
            id = data._id,
            name = data.name
        )
    }
}

fun SecondaryConstructor.convertSecondaryTypeToConstructorType(): List<ConstructorType> {
    var counter = 1
    val convertedList = this@convertSecondaryTypeToConstructorType.data.map { data ->
        ConstructorType(
            id = data._id,
            serie = data.device.serie,
            numbering = counter.toString(),
            level = data.level.toString(),
            preassur = data.pressure.toString(),
            volume = data.volume.toString(),
            signal = if (data.signal == "good") true else false,
            dateInMillisecond = data.date_in_ms.toString(),
            regionId = ""
        ).also { counter++ }
    }

    return convertedList
}

fun OwnerSecondaryType.convertOwnerSecondaryToOwnerType(): List<OwnerType> {
    return this@convertOwnerSecondaryToOwnerType.data.map { data ->
        OwnerType(
            id = data.id,
            firstName = data.first_name,
            lastName = data.last_name,
            region = data.region,
            role = data.role,
            username = data.username
        )
    }
}

fun Uri.createPartFromUri(context: Context): MultipartBody.Part {
    // Get the content resolver from the context
    val contentResolver = context.contentResolver
    // Get the MIME type of the file
    val mimeType = contentResolver.getType(this@createPartFromUri)
    // Get the file name from the URI
    val fileName = DocumentFile.fromSingleUri(context, this@createPartFromUri)?.name
    // Get the input stream from the URI
    val inputStream = contentResolver.openInputStream(this@createPartFromUri)
    // Create a byte array output stream
    val outputStream = ByteArrayOutputStream()
    // Copy the input stream to the output stream
    inputStream?.copyTo(outputStream)
    // Create a request body from the output stream and the MIME type
    val requestBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), outputStream.toByteArray())
    // Create a multipart body part from the request body and the file name
    return MultipartBody.Part.createFormData("file", fileName, requestBody)
}

fun String.convertToRequestBody(): RequestBody {
    return this@convertToRequestBody.toRequestBody(MultipartBody.FORM)
}