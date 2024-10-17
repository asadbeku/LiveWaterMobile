package uz.prestige.livewater.level.device.add_device.view_model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.device.type.DeviceDataPassType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.convertOwnerSecondaryToOwnerType
import uz.prestige.livewater.utils.convertRegionSecondaryToRegionType
import uz.prestige.livewater.utils.convertToRequestBody
import uz.prestige.livewater.utils.createPartFromUri
import javax.inject.Inject

class AddDeviceRepository @Inject constructor(
    private val apiService: ApiService
) {

    // Fetch regions from the network
    fun getRegions() = flow {
        val response = apiService.getRegions()
        emit(response.body()?.convertRegionSecondaryToRegionType())
    }

    // Fetch owners from the network
    fun getOwners() = flow {
        val response = apiService.getOwners()
        emit(response.body()?.convertOwnerSecondaryToOwnerType())
    }

    // Add a new device
    suspend fun addNewDevice(context: Context, device: DeviceDataPassType) = flow {
        val file = device.uri?.createPartFromUri(context)

        val serie = device.serialNumber.convertToRequestBody()
        val name = device.objectName.convertToRequestBody()
        val devicePrivateKey = device.privateKey.convertToRequestBody()
        val region = device.regionId.convertToRequestBody()
        val owner = device.ownerId.convertToRequestBody()
        val lat = DEFAULT_LATITUDE.convertToRequestBody()
        val long = DEFAULT_LONGITUDE.convertToRequestBody()

        val response = file?.let {
            apiService.addNewDevice(serie, name, devicePrivateKey, region, owner, lat, long, it)
        }

        response?.let {
            Log.d(TAG, "${it.body()}")
        }

        emit("")
    }

    // Change device information
    fun changeDeviceInfo(context: Context, device: DeviceDataPassType) = flow {

        val id = device.deviceId
        val serie = device.serialNumber.convertToRequestBody()
        val name = device.objectName.convertToRequestBody()
        val devicePrivateKey = device.privateKey.convertToRequestBody()
        val region = device.regionId.convertToRequestBody()
        val owner = device.ownerId.convertToRequestBody()
        val lat = DEFAULT_LATITUDE.convertToRequestBody()
        val long = DEFAULT_LONGITUDE.convertToRequestBody()

        val response = if (device.uri != null) {
            val file = device.uri.createPartFromUri(context)
            apiService.changeDeviceInfoWithFile(
                id,
                serie,
                name,
                devicePrivateKey,
                region,
                owner,
                lat,
                long,
                file
            )
        } else {
            apiService.changeDeviceInfo(id, serie, name, devicePrivateKey, region, owner, lat, long)
        }

        if (response.isSuccessful) emit(true) else emit(false)
    }

    companion object {
        private const val TAG = "AddDeviceRepository"
        private const val DEFAULT_LATITUDE = "777"
        private const val DEFAULT_LONGITUDE = "88"
    }
}
