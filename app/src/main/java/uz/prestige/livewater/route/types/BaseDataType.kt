package uz.prestige.livewater.route.types

data class BaseDataType(
    val id: String,
    val level: Int,
    val volume: Double,
    val salinity: Double,
    val date: Long
)
