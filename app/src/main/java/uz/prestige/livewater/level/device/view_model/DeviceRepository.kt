package uz.prestige.livewater.level.device.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.convertDeviceSecondaryToDeviceType

class DeviceRepository {


//    suspend fun getDevicesList() = flow {
//        kotlinx.coroutines.delay(1000)
//
//        emit(list)
//    }

    suspend fun getDevicesList() = flow {

        val response = NetworkLevel.buildService(ApiService::class.java).getDevices(20)

        if (response.isSuccessful) {
            emit(response.body()!!.convertDeviceSecondaryToDeviceType())
        } else {
            throw Exception("${response.errorBody()}")
        }

    }

}