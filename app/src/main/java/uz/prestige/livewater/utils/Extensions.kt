package uz.prestige.livewater.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Long.toFormattedTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this)
    return dateFormat.format(date)
}