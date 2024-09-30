package uz.prestige.livewater.level.device.type

data class OwnerType(
    val id: String,
    val firstName: String,
    val lastName: String,
    val region: String,
    val role: String,
    val username: String
)