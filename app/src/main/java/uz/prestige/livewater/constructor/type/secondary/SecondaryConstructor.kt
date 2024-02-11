package uz.prestige.livewater.constructor.type.secondary

data class SecondaryConstructor(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)