package uz.prestige.livewater.level.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemLastInfoBinding
import uz.prestige.livewater.utils.toFormattedTime

abstract class LastUpdateBaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemLastInfoBinding.bind(view)

    init {

    }

    fun bind(numbering: String, serial: String,level: String, salinity: String, volume: String, signal: Boolean, time: String, name: String) {
        binding.numbering.text = numbering
        binding.deviceObjectName.text = name
        binding.level.text = level
        binding.pressure.text = salinity
        binding.volume.text = volume

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