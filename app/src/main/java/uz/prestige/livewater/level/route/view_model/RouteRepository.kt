package uz.prestige.livewater.level.route.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.convertToBaseDataType
import uz.prestige.livewater.utils.convertToRouteType
import javax.inject.Inject

class RouteRepository @Inject constructor(private val apiService: ApiService) {

    private var routeList = mutableListOf<String>()

    suspend fun getBaseDataById(id: String) = flow {

        val response = apiService.getRouteInfoById(id)

        emit(response.body()!!.convertToBaseDataType())
    }

    fun getRouteIdByPosition(position: Int): String = routeList[position]
    fun saveRouteId(id: String) = routeList.add(id)

}