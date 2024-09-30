package uz.prestige.livewater.level.route.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.convertToBaseDataType
import uz.prestige.livewater.utils.convertToRouteType

class RouteRepository {

    suspend fun getRouteListFlow() = flow {

        val response = NetworkLevel.buildService(ApiService::class.java).getRouteData(100, 0)

        emit(response.body()!!.convertToRouteType())
    }

    suspend fun getBaseDataById(id: String) = flow {

        val response = NetworkLevel.buildService(ApiService::class.java).getRouteInfoById(id)

        emit(response.body()!!.convertToBaseDataType())
    }

}