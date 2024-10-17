package uz.prestige.livewater.dayver.device.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.utils.convertDayverDeviceSecondaryToDeviceType
import uz.prestige.livewater.utils.convertToDeviceType
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val apiService: ApiServiceDayver
) {

    private val deviceIds = mutableListOf<String>()

    suspend fun getDevicesList() = flow {
        val response = apiService.getDayverDevices(20, 0)

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
        val response = apiService.getDayverDeviceById(id)

        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!.convertToDeviceType())
        } else {
            throw Exception("Network error")
        }
    }
}
