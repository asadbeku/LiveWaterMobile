package uz.prestige.livewater.constructor.view_model

import uz.prestige.livewater.constructor.type.DeviceType
import android.util.Log
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.constructor.type.ConstructorType
import uz.prestige.livewater.constructor.type.RegionType
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.utils.convertDeviceSecondaryToDeviceType
import uz.prestige.livewater.utils.convertRegionSecondaryToRegionType
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
            regionId == "all" && deviceSerial == "all" -> Network.buildService(ApiService::class.java)
                .getConstructorByNone(
                    offset = offset,
                    limit = limit,
                    start = startTime,
                    end = endTime
                )

            regionId == "all" && deviceSerial != "all" -> Network.buildService(ApiService::class.java)
                .getConstructorByDeviceSerial(
                    offset = offset,
                    limit = limit,
                    start = startTime,
                    end = endTime,
                    device = deviceSerial
                )

            regionId != "all" && deviceSerial == "all" -> Network.buildService(ApiService::class.java)
                .getConstructorByRegion(
                    offset = offset,
                    limit = limit,
                    start = startTime,
                    end = endTime,
                    region = regionId
                )

            else -> Network.buildService(ApiService::class.java).getConstructor(
                offset = offset,
                limit = limit,
                start = startTime,
                end = endTime,
                device = deviceSerial,
                region = regionId
            )
        }

        Log.d("ConstructorRepository", "getConstructorList: ${response.body()}")

        emit((response.body()!!).convertSecondaryTypeToConstructorType())
    }

    suspend fun getRegions() = flow {

        val response = Network.buildService(ApiService::class.java).getRegions()

        regionList = response.body()!!.convertRegionSecondaryToRegionType()

        emit(regionList)
    }

    suspend fun getDevicesSerialByRegion(regionId: String) = flow {
        val response = Network.buildService(ApiService::class.java).getDevices(limit = 100)

        devicesList = response.body()!!.convertDeviceSecondaryToDeviceType()

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
