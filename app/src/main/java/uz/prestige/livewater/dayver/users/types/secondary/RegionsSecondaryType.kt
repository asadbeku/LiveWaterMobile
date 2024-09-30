package uz.prestige.livewater.dayver.users.types.secondary

data class RegionsSecondaryType(
    val `data`: List<DataX>,
    val limit: Int,
    val offset: Int,
    val total: Int
)