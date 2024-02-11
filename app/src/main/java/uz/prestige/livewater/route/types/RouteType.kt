package uz.prestige.livewater.route.types

data class RouteType(
    val id: String,
    val date: Long,
    val privateKey: String,
    val statusCode: Int,
    val message: String,
    val baseDataId: String
)