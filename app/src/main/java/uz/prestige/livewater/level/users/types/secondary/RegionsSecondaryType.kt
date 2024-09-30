package uz.prestige.livewater.level.users.types.secondary

import uz.prestige.livewater.dayver.users.types.secondary.DataX

data class RegionsSecondaryType(
    val `data`: List<DataX>,
    val limit: Int,
    val offset: Int,
    val total: Int
)