package uz.prestige.livewater.home.types

data class LastUpdateType(
    val id: String?,
    val serial: String?,
    val numbering: Int?,
    val level: String?,
    val pressure: String?,
    val volume: String?,
    val signal: Boolean,
    val time: String?
)