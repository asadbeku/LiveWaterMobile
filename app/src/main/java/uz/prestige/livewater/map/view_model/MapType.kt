package uz.prestige.livewater.map.view_model

import android.icu.text.CaseMap.Title
import com.yandex.mapkit.geometry.Point

data class MapType(
    val title: String,
    val point: Point,
    val isWorking: Boolean
)