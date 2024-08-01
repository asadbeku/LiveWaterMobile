package uz.prestige.livewater.test.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.DeviceTestNetwork
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.utils.convertTestTypeToLastUpdates

class TestDeviceRepository {
    suspend fun checkBySerialNumber(serialNumber: String) = flow {
        val response = Network.buildService(ApiService::class.java).getLastUpdate()

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