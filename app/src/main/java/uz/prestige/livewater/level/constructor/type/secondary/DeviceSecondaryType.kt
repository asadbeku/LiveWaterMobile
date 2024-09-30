package uz.prestige.livewater.level.constructor.type.secondary

data class DeviceSecondaryType(
    val `data`: List<DataXX>,
    val limit: Int,
    val offset: Int,
    val total: Int
)