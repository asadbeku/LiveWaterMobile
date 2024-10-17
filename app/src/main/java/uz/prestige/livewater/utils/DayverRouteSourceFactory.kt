package uz.prestige.livewater.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.dayver.route.types.RouteType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.dayver.network.NetworkDayver
import javax.inject.Inject

class DayverRouteSourceFactory @Inject constructor(
    private val apiService: ApiServiceDayver
) : PagingSource<Int, RouteType>() {

    private val limit = 10

    override fun getRefreshKey(state: PagingState<Int, RouteType>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition = anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RouteType> {
        val page = params.key ?: 0

        return try {
            val response = apiService.getDayverRouteData(limit = limit, offset = page)

            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.data

                if (data.isNotEmpty()) {
                    LoadResult.Page(
                        data = response.body()!!.convertToDayverRouteType(),
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