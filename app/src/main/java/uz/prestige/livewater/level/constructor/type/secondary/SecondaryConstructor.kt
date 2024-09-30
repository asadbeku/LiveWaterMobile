package uz.prestige.livewater.level.constructor.type.secondary

data class SecondaryConstructor(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)