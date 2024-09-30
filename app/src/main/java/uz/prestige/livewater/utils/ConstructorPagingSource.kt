package uz.prestige.livewater.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.prestige.livewater.level.constructor.type.ConstructorType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel

class ConstructorPagingSource(
    private val startTime: String,
    private val endTime: String,
    private val regionId: String,
    private val deviceSerial: String
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
            val limit = 10

            val response = when {
                regionId == "all" && deviceSerial == "all" -> NetworkLevel.buildService(ApiService::class.java)
                    .getConstructorByNone(
                        offset = page,
                        limit = limit,
                        start = startTime,
                        end = endTime
                    )

                regionId == "all" && deviceSerial != "all" -> NetworkLevel.buildService(ApiService::class.java)
                    .getConstructorByDeviceSerial(
                        offset = page,
                        limit = limit,
                        start = startTime,
                        end = endTime,
                        device = deviceSerial
                    )

                regionId != "all" && deviceSerial == "all" -> NetworkLevel.buildService(ApiService::class.java)
                    .getConstructorByRegion(
                        offset = page,
                        limit = limit,
                        start = startTime,
                        end = endTime,
                        region = regionId
                    )

                else -> NetworkLevel.buildService(ApiService::class.java).getConstructor(
                    offset = page,
                    limit = limit,
                    start = startTime,
                    end = endTime,
                    device = deviceSerial,
                    region = regionId
                )
            }

            if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    data = response.body()!!.convertSecondaryTypeToConstructorType(),
                    prevKey = if (page == 0) null else (page - 1),
                    nextKey = if (page > response.body()!!.limit) null else (page + 1)
                )
            } else {
                LoadResult.Error(throw Exception("No Response"))
            }
        } catch (e: Exception) {
            Log.e("LoadData", "Error loading data", e)
            LoadResult.Error(e)
        }
    }


}
