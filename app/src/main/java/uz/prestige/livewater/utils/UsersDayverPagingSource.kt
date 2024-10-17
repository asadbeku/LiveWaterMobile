package uz.prestige.livewater.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.dayver.users.types.DayverUserType
import javax.inject.Inject

class UsersDayverPagingSource @Inject constructor(
    private val apiService: ApiServiceDayver
) : PagingSource<Int, DayverUserType>() {
    private val limit: Int = 10  // Default limit

    override fun getRefreshKey(state: PagingState<Int, DayverUserType>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DayverUserType> {
        val page = params.key ?: 0

        return try {
            val response = apiService.getDayverUsers(offset = page, limit = limit)

            if (response.isSuccessful) {
                response.body()?.data?.let { responseData ->
                    return if (responseData.isNotEmpty()) {
                        LoadResult.Page(
                            data = response.body()!!.convertToUserType(),
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
            LoadResult.Error(Exception("Error: ${e.message}"))
        }
    }
}