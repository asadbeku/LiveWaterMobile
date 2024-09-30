package uz.prestige.livewater.dayver.route.types.secondary

data class BaseDataByIdSecondaryType(
    val _id: String,
    val created_at: String,
    val date_in_ms: Long,
    val device: String,
    val level: Double,
    val pressure: Double,
    val signal: String,
    val updated_at: String,
    val volume: Double
)