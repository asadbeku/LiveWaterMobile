package uz.prestige.livewater.route.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.route.types.BaseDataType
import uz.prestige.livewater.route.types.RouteType

class RouteRepository {

    private val list = listOf(
        RouteType(
            "659657702a368e42e334f0d7",
            1704351600050,
            "ta9y8d6bm",
            200,
            "Error saving data",
            "659657702a368e42e334f09b"
        ),
        RouteType(
            "659657702a368e42e334f0d5",
            1704351600050,
            "fuj1fme2",
            500,
            "Error saving data",
            "659657702a368e42e334f097"
        ),
        RouteType(
            "659657702a368e42e334f0d3",
            1704351600050,
            "gv0n0q3jj",
            400,
            "Error saving data",
            "659657702a368e42e334f095"
        ),
        RouteType(
            "659657702a368e42e334f0d1",
            1704351600050,
            "ndc964w0s",
            300,
            "Error saving data",
            "659657702a368e42e334f093"
        ),
        RouteType(
            "659657702a368e42e334f0d7",
            1704351600050,
            "ta9y8d6bm",
            200,
            "Error saving data",
            "659657702a368e42e334f09b"
        ),
        RouteType(
            "659657702a368e42e334f0d5",
            1704351600050,
            "fuj1fme2",
            500,
            "Error saving data",
            "659657702a368e42e334f097"
        ),
        RouteType(
            "659657702a368e42e334f0d3",
            1704351600050,
            "gv0n0q3jj",
            400,
            "Error saving data",
            "659657702a368e42e334f095"
        ),
        RouteType(
            "659657702a368e42e334f0d1",
            1704351600050,
            "ndc964w0s",
            300,
            "Error saving data",
            "659657702a368e42e334f093"
        )
    )

    suspend fun getRouteListFlow() = flow {
        kotlinx.coroutines.delay(1000)
        emit(list)
    }

    suspend fun getBaseDataById(id: String) = flow {

        val type = BaseDataType("659505f00f4cd53dba04e744", 37, 0.61.toDouble(), 10.toDouble(), 1704265200054)

        kotlinx.coroutines.delay(1000)
        emit(type)
    }

}