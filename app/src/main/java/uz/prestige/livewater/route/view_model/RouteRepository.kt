package uz.prestige.livewater.route.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.route.types.BaseDataType
import uz.prestige.livewater.route.types.RouteType
import uz.prestige.livewater.utils.convertToBaseDataType
import uz.prestige.livewater.utils.convertToRouteType

class RouteRepository {

    suspend fun getRouteListFlow() = flow {

        val response = Network.buildService(ApiService::class.java).getRouteData(100, 0)

        emit(response.body()!!.convertToRouteType())
    }

    suspend fun getBaseDataById(id: String) = flow {

        val response = Network.buildService(ApiService::class.java).getRouteInfoById(id)

        emit(response.body()!!.convertToBaseDataType())
    }

}