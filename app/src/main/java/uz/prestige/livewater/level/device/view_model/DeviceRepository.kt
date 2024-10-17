package uz.prestige.livewater.level.device.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.convertDeviceSecondaryToDeviceType
import uz.prestige.livewater.utils.convertToDeviceType
import javax.inject.Inject

class DeviceRepository @Inject constructor(private val apiService: ApiService) {

    private val deviceIdList = mutableListOf<String>()

    suspend fun getDevicesList() = flow {
        val response = apiService.getDevices(limit = 100, offset = 0)

        if (response.isSuccessful) {
            response.body()?.let { devices ->
                emit(devices.convertDeviceSecondaryToDeviceType())
            } ?: throw Exception("No device data available")
        } else {
            throw Exception("Error: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getDeviceDataById(id: String) = flow {
        val response = apiService.getDeviceById(id)

        if (response.isSuccessful) {
            response.body()?.let { device ->
                emit(device.convertToDeviceType())
            } ?: throw Exception("No device data for ID: $id")
        } else {
            throw Exception("Error: ${response.errorBody()?.string()}")
        }
    }

    fun getDeviceId(position: Int): String {
        return deviceIdList.getOrNull(position)
            ?: throw IndexOutOfBoundsException("Invalid position: $position")
    }

    fun saveDeviceId(deviceId: String) {
        if (deviceId !in deviceIdList) {
            deviceIdList.add(deviceId)
        }
    }
}
