package uz.prestige.livewater.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.dayver.constructor.type.DeviceType
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.dayver.network.NetworkDayver
import javax.inject.Inject

class DayverDeviceSourceFactory @Inject constructor(
    private val apiService: ApiServiceDayver
) : PagingSource<Int, DeviceType>() {

    private val limit = 10

    override fun getRefreshKey(state: PagingState<Int, DeviceType>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DeviceType> {
        return try {
            val page = params.key ?: 0

            val response = apiService.getDayverDevices(limit = limit, offset = page)

            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.data
                if (response.body()!!.data.isNotEmpty()) {
                    LoadResult.Page(
                        data = response.body()!!.convertDayverDeviceSecondaryToDeviceType(),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (data.size < limit) null else page + 1
                    )
                } else {
                    LoadResult.Error(Exception("Empty data"))
                }
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} - ${response.message()}"))
            }


        } catch (e: Exception) {
            LoadResult.Error(Exception(e.message))
        }
    }
}