package uz.prestige.livewater.level.home.types.test

data class Data(
    val _id: String,
    val created_at: String,
    val date_in_ms: Long,
    val device: Device,
    val level: Double,
    val pressure: Double,
    val signal: String,
    val updated_at: String,
    val volume: Double
)