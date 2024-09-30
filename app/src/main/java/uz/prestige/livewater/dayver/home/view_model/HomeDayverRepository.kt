package uz.prestige.livewater.dayver.home.view_model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.types.LastUpdateTypeDayver
import uz.prestige.livewater.level.home.types.DeviceStatuses
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertSecondaryToPrimary

class HomeDayverRepository {

    suspend fun getDevicesStatusesFlow(): Flow<DeviceStatuses> = flow {
        val updates = getLastUpdates().firstOrNull() ?: emptyList()
        val activeCount = updates.count { it.signal }
        val totalCount = updates.size
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

    suspend fun getLastUpdates(): Flow<List<LastUpdateTypeDayver>> = flow {
        try {
            val response = NetworkDayver.buildService(ApiService::class.java).getLastUpdateDayver()
            if (response.isSuccessful) {
                val list = response.body()?.convertSecondaryToPrimary() ?: emptyList()
                Log.d("HomeDayverRepository", "Received updates: $list")
                emit(list)
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
