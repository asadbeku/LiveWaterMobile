package uz.prestige.livewater.home.types.test

data class LastUpdateTestType(
    val `data`: List<Data>,
    val limit: Int,
    val offset: Int,
    val total: Int
)