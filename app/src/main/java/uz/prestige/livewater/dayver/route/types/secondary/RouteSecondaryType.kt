package uz.prestige.livewater.dayver.route.types.secondary

data class RouteSecondaryType(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)