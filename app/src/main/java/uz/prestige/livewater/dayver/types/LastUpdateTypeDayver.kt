package uz.prestige.livewater.dayver.types

data class LastUpdateTypeDayver(
    val id: String?,
    val serial: String?,
    val name: String,
    val numbering: String,
    val level: String?,
    val salinity: String?,
    val temperature: String?,
    val signal: Boolean,
    val time: String?
)
