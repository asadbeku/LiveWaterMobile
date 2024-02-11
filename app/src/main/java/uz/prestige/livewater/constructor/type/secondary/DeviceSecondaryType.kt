package uz.prestige.livewater.constructor.type.secondary

data class DeviceSecondaryType(
    val `data`: List<DataXX>,
    val limit: Int,
    val offset: Int,
    val total: Int
)