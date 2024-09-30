package uz.prestige.livewater.level.route.types.secondary

data class Data(
    val _id: String,
    val basedata: String,
    val created_at: String,
    val device_privet_key: String,
    val message: String,
    val send_data_in_ms: Long,
    val status_code: Int,
    val updated_at: String
)