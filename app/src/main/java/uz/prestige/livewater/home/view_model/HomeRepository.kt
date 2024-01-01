package uz.prestige.livewater.home.view_model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.home.types.DeviceLastUpdate
import uz.prestige.livewater.home.types.DeviceStatuses

class HomeRepository {

    private val lastUpdates = listOf(
        DeviceLastUpdate(
            id = "6591ac26a10493d603308c19",
            serial = "869300038366344",
            numbering = 1,
            level = "42",
            salinity = "9",
            volume = "0.74",
            signal = true,
            time = "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac23a10493d603308c13",
            "868221040593660",
            2,
            "11",
            "3",
            "0.09",
            true,
            "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac23a10493d603308c11",
            "868221040671953",
            3,
            "28",
            "11",
            "0.39",
            true,
            "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac23a10493d603308c0d",
            "869300038374736",
            4,
            "46",
            "5",
            "0.58",
            true,
            "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac22a10493d603308c09",
            "869300038390617",
            5,
            "11",
            "4",
            "0.09",
            false,
            "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac22a10493d603308bfd",
            "869300038354902",
            6,
            "28",
            "6",
            "0.39",
            true,
            "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac22a10493d603308bf9",
            "869300038353599",
            7,
            "59",
            "8",
            "1.25",
            true,
            "1704045600042"
        ),
        DeviceLastUpdate(
            "6591ac22a10493d603308bf7",
            "864333048051635",
            8,
            "17",
            "9",
            "0.63",
            true,
            "1704045600042"
        )
    )

    fun getDevicesStatusesFlow(): Flow<DeviceStatuses> = flow {
        val active = "7"
        val inActive = "1"
        val all = "8"
        emit(DeviceStatuses(all, active, inActive))
    }

    suspend fun getLastUpdates() = flow {
        emit(lastUpdates)
    }

}