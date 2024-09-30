package uz.prestige.livewater.dayver.users.types.secondary

data class DayverUserSecondaryType(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)