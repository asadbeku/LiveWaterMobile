package uz.prestige.livewater.dayver.device.add_device.view_model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.device.type.DeviceDataPassType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.convertDayverOwnerSecondaryToOwnerType
import uz.prestige.livewater.utils.convertOwnerSecondaryToOwnerType
import uz.prestige.livewater.utils.convertRegionSecondaryToRegionType
import uz.prestige.livewater.utils.convertToRequestBody
import uz.prestige.livewater.utils.createPartFromUri

class AddDeviceRepository {

    // Fetch regions from the network
    fun getRegions() = flow {
        val response = NetworkLevel.buildService(ApiService::class.java).getRegions()
        emit(response.body()?.convertRegionSecondaryToRegionType())
    }

    // Fetch owners from the network
    fun getOwners() = flow {
        val response = NetworkLevel.buildService(ApiService::class.java).getDayverOwners()
        emit(response.body()?.convertDayverOwnerSecondaryToOwnerType())
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
            NetworkLevel.buildService(ApiService::class.java)
                .addNewDevice(serie, name, devicePrivateKey, region, owner, lat, long, it)
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
            NetworkLevel.buildService(ApiService::class.java)
                .changeDeviceInfoWithFile(
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
            NetworkLevel.buildService(ApiService::class.java)
                .changeDeviceInfo(id, serie, name, devicePrivateKey, region, owner, lat, long)
        }

        if (response.isSuccessful) emit(true) else emit(false)
    }

    companion object {
        private const val TAG = "AddDeviceRepository"
        private const val DEFAULT_LATITUDE = "777"
        private const val DEFAULT_LONGITUDE = "88"
    }
}
