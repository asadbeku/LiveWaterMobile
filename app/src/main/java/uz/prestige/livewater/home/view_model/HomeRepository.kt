package uz.prestige.livewater.home.view_model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.home.types.LastUpdateType
import uz.prestige.livewater.home.types.DeviceStatuses
import uz.prestige.livewater.home.types.test.LastUpdateTestType
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network

class HomeRepository {
    private var all = "null"
    private var active = "null"

    suspend fun getDevicesStatusesFlow(): Flow<DeviceStatuses> = flow {
        val active = active
        val inActive = (all.toInt() - active.toInt()).toString()
        val all = all
        emit(DeviceStatuses(all, active, inActive))
    }

    suspend fun getLastUpdates() = flow {

        val response = Network.buildService(ApiService::class.java).getLastUpdate(50)

        val list = convertTestTypeToLastUpdates(response.body()!!)

        val counter = list.map { device ->
            if (device.signal) 1 else 0
        }.sum()

        active = counter.toString()
        all = list.size.toString()

        emit(list)
    }

    private fun convertTestTypeToLastUpdates(list: LastUpdateTestType): List<LastUpdateType> {
        var counter = 1
        val convertedList = list.data.map { data ->
            LastUpdateType(
                id = data._id,
                serial = data.device.serie,
                numbering = counter,
                level = data.level.toString(),
                pressure = data.pressure.toString(),
                volume = data.volume.toString(),
                signal = if (data.signal == "good") true else false,
                time = data.date_in_ms.toString()
            ).also { counter++ }
        }

        return convertedList
    }
}