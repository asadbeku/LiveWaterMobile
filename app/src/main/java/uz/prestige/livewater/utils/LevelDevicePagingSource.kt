package uz.prestige.livewater.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.level.constructor.type.DeviceType
import uz.prestige.livewater.level.network.ApiService
import javax.inject.Inject

class LevelDevicePagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, DeviceType>() {

    private val limit: Int = 6  // Default limit

    override fun getRefreshKey(state: PagingState<Int, DeviceType>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DeviceType> {
        return try {
            val page = params.key ?: 0
            val response = apiService.getDevices(page, limit)

            if (response.isSuccessful) {
                response.body()?.data?.let { responseData ->
                    return if (responseData.isNotEmpty()) {
                        LoadResult.Page(
                            data = response.body()!!.convertDeviceSecondaryToDeviceType(),
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
            LoadResult.Error(e)
        }
    }

}