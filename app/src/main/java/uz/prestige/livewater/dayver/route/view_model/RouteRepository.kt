package uz.prestige.livewater.dayver.route.view_model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.route.types.BaseDataType
import uz.prestige.livewater.dayver.route.types.RouteType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertToDayverBaseDataType
import uz.prestige.livewater.utils.convertToDayverRouteType

class RouteRepository {

    private val routeIds = mutableListOf<String>()

    suspend fun getRouteListFlow(): Flow<List<RouteType>> = flow {
        try {
            val response =
                NetworkDayver.buildService(ApiService::class.java).getDayverRouteData(100, 0)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!.convertToDayverRouteType())
            } else {
                Log.e(
                    "RouteRepository",
                    "Error fetching route list: ${response.errorBody()?.string()}"
                )
                emit(emptyList())  // Emit an empty list in case of an error
                throw Exception("Error fetching route list: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("RouteRepository", "Exception fetching route list: ${e.message}")
            emit(emptyList())  // Emit an empty list on exception
            throw Exception("Exception fetching route list: ${e.message}")
        }
    }

    suspend fun getBaseDataById(id: String): Flow<BaseDataType> = flow {
        try {
            val response =
                NetworkDayver.buildService(ApiService::class.java).getDayverRouteInfoById(id)
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
