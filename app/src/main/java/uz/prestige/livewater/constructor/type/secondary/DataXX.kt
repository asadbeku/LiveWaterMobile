package uz.prestige.livewater.constructor.type.secondary

data class DataXX(
    val _id: String,
    val created_at: String,
    val device_privet_key: String,
    val ip_address: String,
    val lat: Double,
    val long: Double,
    val owner: Owner?,
    val name: String,
    val port: Int,
    val region: Region,
    val serie: String,
    val updated_at: String,
    val isWorking: Boolean
)