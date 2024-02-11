package uz.prestige.livewater.device.add_device.view_model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import uz.prestige.livewater.device.type.DeviceDataPassType
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.utils.convertOwnerSecondaryToOwnerType
import uz.prestige.livewater.utils.convertRegionSecondaryToRegionType
import uz.prestige.livewater.utils.convertToRequestBody
import uz.prestige.livewater.utils.createPartFromUri

class AddDeviceRepository {

    fun getRegions() = flow {

        val response = Network.buildService(ApiService::class.java).getRegions()

        emit(response.body()!!.convertRegionSecondaryToRegionType())

    }

    fun getOwners() = flow {

        val response = Network.buildService(ApiService::class.java).getOwners()

        emit(response.body()!!.convertOwnerSecondaryToOwnerType())
    }

    suspend fun addNewDevice(context: Context, device: DeviceDataPassType) = flow {

        val file = device.uri.createPartFromUri(context)

        val serie = device.serialNumber.convertToRequestBody()
        val name = device.objectName.convertToRequestBody()
        val devicePrivateKey = device.privateKey.convertToRequestBody()
        val region = device.regionId.convertToRequestBody()
        val owner = device.ownerId.convertToRequestBody()
        val lat = "777".convertToRequestBody()
        val long = "88".convertToRequestBody()

        val response = Network.buildService(ApiService::class.java)
            .addNewDevice(
                serie,
                name,
                devicePrivateKey,
                region,
                owner,
                lat,
                long,
                file
            )

        Log.d("addNewDevice", "${response.body()}")

        emit("")
    }


}