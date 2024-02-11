package uz.prestige.livewater.device.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.constructor.type.DeviceType
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.utils.convertDeviceSecondaryToDeviceType

class DeviceRepository {


//    suspend fun getDevicesList() = flow {
//        kotlinx.coroutines.delay(1000)
//
//        emit(list)
//    }

    suspend fun getDevicesList() = flow {

        val response = Network.buildService(ApiService::class.java).getDevices(20)

        if (response.isSuccessful) {
            emit(response.body()!!.convertDeviceSecondaryToDeviceType())
        } else {
            throw Exception("${response.errorBody()}")
        }

    }

}