package uz.prestige.livewater.dayver.constructor.view_model

import android.util.Log
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.constructor.type.ConstructorType
import uz.prestige.livewater.dayver.constructor.type.DeviceType
import uz.prestige.livewater.dayver.constructor.type.RegionType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertDayverDeviceSecondaryToDeviceType
import uz.prestige.livewater.utils.convertDayverRegionSecondaryToRegionType
import uz.prestige.livewater.utils.convertDayverSecondaryTypeToConstructorType
import uz.prestige.livewater.utils.convertSecondaryTypeToConstructorType

class ConstructorRepository {

    private var listConstructor = listOf<ConstructorType>()

    private var regionList = listOf<RegionType>()

    private var devicesList = listOf<DeviceType>()

    suspend fun getConstructorList(
        offset: Int,
        limit: Int,
        startTime: String,
        endTime: String,
        regionId: String,
        deviceSerial: String
    ) = flow {

        val response = when {
            regionId == "all" && deviceSerial == "all" -> NetworkDayver.buildService(ApiService::class.java)
                .getDayverConstructorByNone(
                    offset = offset,
                    limit = limit,
                    start = startTime,
                    end = endTime
                )

            regionId == "all" && deviceSerial != "all" -> NetworkDayver.buildService(ApiService::class.java)
                .getDayverConstructorByDeviceSerial(
                    offset = offset,
                    limit = limit,
                    start = startTime,
                    end = endTime,
                    device = deviceSerial
                )

            regionId != "all" && deviceSerial == "all" -> NetworkDayver.buildService(ApiService::class.java)
                .getDayverConstructorByRegion(
                    offset = offset,
                    limit = limit,
                    start = startTime,
                    end = endTime,
                    region = regionId
                )

            else -> NetworkDayver.buildService(ApiService::class.java).getDayverConstructor(
                offset = offset,
                limit = limit,
                start = startTime,
                end = endTime,
                device = deviceSerial,
                region = regionId
            )
        }

        Log.d("ConstructorRepository", "getConstructorList: ${response.body()}")
        emit((response.body()!!).convertDayverSecondaryTypeToConstructorType())
    }

    suspend fun getRegions() = flow {

        val response = NetworkDayver.buildService(ApiService::class.java).getDayverRegions()

        regionList = response.body()!!.convertDayverRegionSecondaryToRegionType()

        emit(regionList)
    }

    suspend fun getDevicesSerialByRegion(regionId: String) = flow {
        val response =
            NetworkDayver.buildService(ApiService::class.java).getDayverDevices(limit = 100,1)

        devicesList = response.body()!!.convertDayverDeviceSecondaryToDeviceType()

        emit((devicesList))
    }

    suspend fun getDevicesByRegionId(regionId: String) = flow {

        val list = devicesList.filter { it.regionId == regionId }

        emit(list)
    }

    suspend fun getRegionIdByRegionName(regionId: String) = flow {

        val list = devicesList.find { it.regionId == regionId }?.regionId ?: "all"

        emit(list)
    }

    fun getDeviceIdBySerialNumber(deviceSerial: String) =
        devicesList.find { it.serialNumber == deviceSerial }?.id ?: ""
}
