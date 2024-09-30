package uz.prestige.livewater.dayver.map.view_model

import com.yandex.mapkit.geometry.Point

data class MapType(
    val title: String,
    val point: Point,
    val isWorking: Boolean
)