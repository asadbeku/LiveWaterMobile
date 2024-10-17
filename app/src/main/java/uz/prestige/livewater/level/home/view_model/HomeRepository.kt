package uz.prestige.livewater.level.home.view_model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.home.types.DeviceStatuses
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.convertTestTypeToLastUpdates
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService
) {
    private var globalList: List<LastUpdateType> = emptyList()

    suspend fun getDevicesStatusesFlow(): Flow<DeviceStatuses> = flow {
        val activeCount = globalList.count { it.signal }
        val totalCount = globalList.size
        val inactiveCount = totalCount - activeCount

        Log.d(
            "HomeDayverRepository",
            "Device Statuses - Active: $activeCount, Inactive: $inactiveCount, Total: $totalCount"
        )

        emit(
            DeviceStatuses(
                all = totalCount.toString(),
                active = activeCount.toString(),
                inActive = inactiveCount.toString()
            )
        )
    }

    suspend fun getLastUpdates(): Flow<List<LastUpdateType>> = flow {
        try {
            val response = apiService.getLastUpdate()

            if (response.isSuccessful) {
                globalList = response.body()?.convertTestTypeToLastUpdates() ?: emptyList()
                Log.d("HomeDayverRepository", "Received updates: $globalList")
                emit(globalList)
            } else {
                Log.e("HomeDayverRepository", "Error fetching updates: ${response.errorBody()}")
                emit(emptyList())
            }

        } catch (e: Exception) {
            Log.e("HomeDayverRepository", "Exception fetching updates: ${e.message}")
            emit(emptyList())
        }
    }
}