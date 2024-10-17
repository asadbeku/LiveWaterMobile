package uz.prestige.livewater.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.dayver.constructor.type.ConstructorType
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.dayver.network.NetworkDayver

class DayverConstructorPagingSource(
    private val apiService: ApiServiceDayver,
    private val startTime: String?,
    private val endTime: String?,
    private val regionId: String?,
    private val deviceSerial: String?,
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

            val response =
                apiService.getBaseData(
                    generateQueryMap(
                        startTime,
                        endTime,
                        regionId,
                        deviceSerial,
                        page,
                        limit
                    )
                )

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
            Log.e(
                "DayverPagingSource",
                "Error loading data. Params: page=$params.key, startTime=$startTime, endTime=$endTime, regionId=$regionId, deviceSerial=$deviceSerial",
                e
            )
            LoadResult.Error(e)
        }
    }

    private fun generateQueryMap(
        startTime: String?,
        endTime: String?,
        regionId: String?,
        deviceSerial: String?,
        page: Int,
        limit: Int
    ): Map<String, String> {
        Log.d(
            "DayverPagingSource",
            "generateQueryMap called with params: $startTime, $endTime, $regionId, $deviceSerial"
        )
        val queryMap = mutableMapOf<String, String>()
        queryMap["page[limit]"] = limit.toString()
        queryMap["page[offset]"] = page.toString()
        startTime?.let { queryMap["filter[start]"] = it }
        endTime?.let { queryMap["filter[end]"] = it }
        regionId?.let { queryMap["filter[region]"] = it }
        deviceSerial?.let { queryMap["filter[device]"] = it }

        return queryMap
    }
}
