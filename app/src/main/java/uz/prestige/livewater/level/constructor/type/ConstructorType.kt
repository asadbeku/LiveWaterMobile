package uz.prestige.livewater.level.constructor.type

data class ConstructorType(
    val id: String,
    val numbering: String,
    val serie: String,
    val level: String,
    val signal: Boolean,
    val preassur: String,
    val dateInMillisecond: String,
    val volume: String,
    val regionId: String,
)
