package uz.prestige.livewater.level.home.types

data class LastUpdateType(
    val id: String?,
    val serial: String?,
    val name: String,
    val numbering: String,
    val level: String?,
    val pressure: String?,
    val volume: String?,
    val signal: Boolean,
    val time: String?
)