package uz.prestige.livewater.level.test.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.TestApiService
import uz.prestige.livewater.utils.convertTestTypeToLastUpdates
import javax.inject.Inject

class TestDeviceRepository @Inject constructor(
    private val apiService: ApiService,
    private val testApiService: TestApiService
) {
    suspend fun checkBySerialNumber(serialNumber: String) = flow {
        val response = apiService.getLastUpdate()

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
            testApiService.makeRequestToDevice(serialNumber)

        if (response.isSuccessful) emit(true) else emit(false)
    }
}