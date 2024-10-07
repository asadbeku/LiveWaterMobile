package uz.prestige.livewater.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.level.route.types.RouteType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel

class RouteConfigPagingSource : PagingSource<Int, RouteType>() {
    private val limit: Int = 3  // Default limit

    override fun getRefreshKey(state: PagingState<Int, RouteType>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RouteType> {
        val page = params.key ?: 0
        return try {
            val response = NetworkLevel.buildService(ApiService::class.java).getRouteData(offset = page, limit = limit)

            if (response.isSuccessful) {
                response.body()?.data?.let { responseData ->
                    return if (responseData.isNotEmpty()) {
                        LoadResult.Page(
                            data = response.body()!!.convertToRouteType(),
                            prevKey = if (page == 0) null else page - 1,
                            nextKey = if (responseData.size < limit) null else page + 1
                        )
                    } else {
                        LoadResult.Error(Exception("No data available"))
                    }
                }
            }
            LoadResult.Error(Exception("Error: ${response.code()} - ${response.message()}"))
        } catch (e: Exception) {
            Log.e("RouteConfigPagingSource", "Load error: ${e.message}")
            LoadResult.Error(e)
        }
    }
}
