package uz.prestige.livewater.dayver.device.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertDeviceSecondaryToDeviceType

class DeviceRepository {

    suspend fun getDevicesList() = flow {
        val response = NetworkDayver.buildService(ApiService::class.java).getDayverDevices(20)

        if (response.isSuccessful) {
            response.body()?.let { deviceList ->
                emit(deviceList.convertDeviceSecondaryToDeviceType())
            } ?: throw Exception("Response body is null")
        } else {
            throw Exception("Error: ${response.code()} - ${response.errorBody()?.string()}")
        }
    }
}
