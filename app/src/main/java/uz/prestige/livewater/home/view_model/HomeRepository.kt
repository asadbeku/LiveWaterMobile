package uz.prestige.livewater.home.view_model

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.home.types.LastUpdateType
import uz.prestige.livewater.home.types.DeviceStatuses
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.utils.ConstructorPagingSource
import uz.prestige.livewater.utils.convertTestTypeToLastUpdates

class HomeRepository {
    private var all = "null"
    private var active = "null"

    suspend fun getDevicesStatusesFlow(): Flow<DeviceStatuses> = flow {
        val active = active
        val inActive = (all.toInt() - active.toInt()).toString()
        val all = all
        emit(DeviceStatuses(all, active, inActive))
    }

    suspend fun getLastUpdates(): Flow<List<LastUpdateType>> = flow {
        val response = Network.buildService(ApiService::class.java).getLastUpdate()
        val list = response.body()?.convertTestTypeToLastUpdates() ?: emptyList()

        val counter = list.count { it.signal }

        active = counter.toString()
        all = list.size.toString()

        emit(list)
    }

    suspend fun crashTest(){
        val response = Network.buildService(ApiService::class.java).getLastUpdate()
    }
}