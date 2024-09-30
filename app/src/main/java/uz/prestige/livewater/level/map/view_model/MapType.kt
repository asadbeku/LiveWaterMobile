package uz.prestige.livewater.level.map.view_model

import android.icu.text.CaseMap.Title
import com.yandex.mapkit.geometry.Point

data class MapType(
    val title: String,
    val point: Point,
    val isWorking: Boolean
)