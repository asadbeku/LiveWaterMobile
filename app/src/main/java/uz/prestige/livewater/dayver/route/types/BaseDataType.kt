package uz.prestige.livewater.dayver.route.types

data class BaseDataType(
    val id: String,
    val level: Double,
    val salinity: Double,
    val temperature: Double,
    val signal: String,
    val device: String,
    val date: Long
)
