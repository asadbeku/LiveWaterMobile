package uz.prestige.livewater.device.type.secondary_type

data class OwnerSecondaryType(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)