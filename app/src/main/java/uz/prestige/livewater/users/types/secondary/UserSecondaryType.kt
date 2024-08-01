package uz.prestige.livewater.users.types.secondary

data class UserSecondaryType(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)