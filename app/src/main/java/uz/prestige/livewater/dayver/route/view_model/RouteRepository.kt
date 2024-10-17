package uz.prestige.livewater.dayver.route.view_model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.dayver.route.types.BaseDataType
import uz.prestige.livewater.dayver.route.types.RouteType
import uz.prestige.livewater.utils.convertToDayverBaseDataType
import uz.prestige.livewater.utils.convertToDayverRouteType
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val apiService: ApiServiceDayver
) {

    private val routeIds = mutableListOf<String>()

    suspend fun getBaseDataById(id: String): Flow<BaseDataType> = flow {
        try {
            val response =
                apiService.getDayverRouteInfoById(id)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!.convertToDayverBaseDataType())
            } else {
                Log.e(
                    "RouteRepository",
                    "Error fetching base data by ID: ${response.errorBody()?.string()}"
                )
                throw Exception("Error fetching base data by ID: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("RouteRepository", "Exception fetching base data: ${e.message}")
            throw Exception("Exception fetching base data: ${e.message}")
        }
    }

    fun saveId(id: String) = routeIds.add(id)

    fun getId(position: Int) = routeIds[position]
}
