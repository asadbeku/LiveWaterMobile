package uz.prestige.livewater.level.users.types.secondary

import uz.prestige.livewater.dayver.users.types.secondary.Device

data class Data(
    val _id: String,
    val created_at: String,
    val devices: List<Device>,
    val first_name: String,
    val id: String,
    val last_name: String,
    val mobil_phone: String,
    val region: String,
    val role: String,
    val updated_at: String,
    val username: String
)