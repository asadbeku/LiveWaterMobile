package uz.prestige.livewater.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.users.types.UserType
import javax.inject.Inject

class UsersPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, UserType>() {
    private val limit: Int = 10  // Default limit

    override fun getRefreshKey(state: PagingState<Int, UserType>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserType> {
        val page = params.key ?: 0

        return try {
            val response = apiService.getLevelUsers(offset = page, limit = limit)

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