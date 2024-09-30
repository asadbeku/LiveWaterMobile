package uz.prestige.livewater.dayver.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemLastInfoDayverBinding
import uz.prestige.livewater.utils.toFormattedTime

abstract class DayverLastUpdateBaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemLastInfoDayverBinding.bind(view)

    init {

    }

    fun bind(numbering: String, serial: String,level: String, salinity: String, temperature: String, signal: Boolean, time: String, name: String) {
        binding.numbering.text = numbering
        binding.deviceObjectName.text = name
        binding.level.text = level
        binding.waterTemperature.text = temperature
        binding.salinity.text = salinity

        setupSignalTypeIcon(signal)

        binding.time.text = time.toLong().toFormattedTime()
    }

    private fun setupSignalTypeIcon(signal: Boolean) {
        val signalText = if (signal) "Yaxshi" else "Signal yo'q"
        val signalIcon = if (signal) R.drawable.icon_circle else R.drawable.icon_circle

        binding.signalTypeText.text = signalText
        binding.signalTypeIcon.setImageResource(signalIcon)

        val tintColor = ContextCompat.getColor(itemView.context, if (signal) R.color.greenPrimary else R.color.redPrimary)
        binding.signalTypeIcon.setColorFilter(tintColor)
    }

}