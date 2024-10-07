package uz.prestige.livewater.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.dayver.constructor.type.ConstructorType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver

class DayverConstructorPagingSource(
    private val startTime: String,
    private val endTime: String,
    private val regionId: String,
    private val deviceSerial: String,
    private val limit: Int = 10  // Limit is now dynamic with a default value of 10
) : PagingSource<Int, ConstructorType>() {

    override fun getRefreshKey(state: PagingState<Int, ConstructorType>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ConstructorType> {
        return try {
            val page: Int = params.key ?: 0

            // Determine which API call to make based on regionId and deviceSerial
            val response = when {
                regionId == "all" && deviceSerial == "all" -> NetworkDayver.buildService(ApiService::class.java)
                    .getDayverConstructorByNone(offset = page, limit = limit, start = startTime, end = endTime)

                regionId == "all" && deviceSerial != "all" -> NetworkDayver.buildService(ApiService::class.java)
                    .getDayverConstructorByDeviceSerial(offset = page, limit = limit, start = startTime, end = endTime, device = deviceSerial)

                regionId != "all" && deviceSerial == "all" -> NetworkDayver.buildService(ApiService::class.java)
                    .getDayverConstructorByRegion(offset = page, limit = limit, start = startTime, end = endTime, region = regionId)

                else -> NetworkDayver.buildService(ApiService::class.java).getDayverConstructor(
                    offset = page,
                    limit = limit,
                    start = startTime,
                    end = endTime,
                    device = deviceSerial,
                    region = regionId
                )
            }

            // Check for successful response and non-empty data
            if (response.isSuccessful && response.body() != null) {
                val responseData = response.body()!!.data
                if (responseData.isNotEmpty()) {
                    LoadResult.Page(
                        data = response.body()!!.convertDayverSecondaryTypeToConstructorType(),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (responseData.size < limit) null else page + 1
                    )
                } else {
                    LoadResult.Error(Exception("Empty data returned"))
                }
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("DayverPagingSource", "Error loading data. Params: page=$params.key, startTime=$startTime, endTime=$endTime, regionId=$regionId, deviceSerial=$deviceSerial", e)
            LoadResult.Error(e)
        }
    }
}
