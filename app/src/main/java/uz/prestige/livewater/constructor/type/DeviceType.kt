package uz.prestige.livewater.constructor.type

data class DeviceType(
    val id: String,
    val numbering: Int,
    val serialNumber: String,
    val objectName: String,
    val devicePrivateKey: String,
    val long: String,
    val lat: String,
    val regionId: String,
    val regionName: String,
    val ownerName: String,
    val createdAt: String
)
