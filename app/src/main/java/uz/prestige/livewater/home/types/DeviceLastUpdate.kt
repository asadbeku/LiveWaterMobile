package uz.prestige.livewater.home.types

data class DeviceLastUpdate(
    val id: String?,
    val serial: String?,
    val numbering: Int?,
    val level: String?,
    val salinity: String?,
    val volume: String?,
    val signal: Boolean,
    val time: String?
)