package uz.prestige.livewater.level.test.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.DeviceTestNetwork
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.convertTestTypeToLastUpdates

class TestDeviceRepository {
    suspend fun checkBySerialNumber(serialNumber: String) = flow {
        val response = NetworkLevel.buildService(ApiService::class.java).getLastUpdate()

        response.body()?.convertTestTypeToLastUpdates()?.map { info ->
            if (info.serial == serialNumber) {
                emit(info)
            } else {
                emit(null)
            }
        }
    }

    suspend fun makeRequestToServer(serialNumber: String) = flow {
        val response =
            DeviceTestNetwork.api.makeRequestToDevice(serialNumber)

        if (response.isSuccessful) emit(true) else emit(false)
    }
}