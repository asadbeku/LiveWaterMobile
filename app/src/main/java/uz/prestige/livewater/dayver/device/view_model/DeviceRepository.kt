package uz.prestige.livewater.dayver.device.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertDayverDeviceSecondaryToDeviceType
import uz.prestige.livewater.utils.convertToDeviceType

class DeviceRepository {

    private val deviceIds = mutableListOf<String>()

    suspend fun getDevicesList() = flow {
        val response = NetworkDayver.buildService(ApiService::class.java).getDayverDevices(20, 1)

        if (response.isSuccessful) {
            response.body()?.let { deviceList ->
                emit(deviceList.convertDayverDeviceSecondaryToDeviceType())
            } ?: throw Exception("Response body is null")
        } else {
            throw Exception("Error: ${response.code()} - ${response.errorBody()?.string()}")
        }
    }

    fun saveIds(id: String) = deviceIds.add(id)

    fun getDeviceId(position: Int) = deviceIds[position]

    fun getDeviceById(id: String) = flow {
        val response = NetworkDayver.buildService(ApiService::class.java).getDayverDeviceById(id)

        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!.convertToDeviceType())
        }else{
            throw Exception("Network error")
        }
    }
}
