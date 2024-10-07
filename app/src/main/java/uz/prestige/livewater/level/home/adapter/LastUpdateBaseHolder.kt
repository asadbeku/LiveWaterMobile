package uz.prestige.livewater.level.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemLastInfoBinding
import uz.prestige.livewater.utils.toFormattedTime

abstract class LastUpdateBaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLastInfoBinding.bind(view)

    fun bind(
        numbering: String,
        serial: String,
        level: String,
        salinity: String,
        volume: String,
        signal: Boolean,
        time: String,
        name: String
    ) {
        with(binding) {
            this.numbering.text = numbering
            deviceObjectName.text = name
            this.level.text = level
            pressure.text = salinity
            this.volume.text = volume
            this.time.text = time.toLong().toFormattedTime()
            setupSignalTypeIcon(signal)
        }
    }

    private fun setupSignalTypeIcon(signal: Boolean) {
        val (signalText, signalIcon, tintColor) = if (signal) {
            Triple("Yaxshi", R.drawable.icon_circle, R.color.greenPrimary)
        } else {
            Triple("Signal yo'q", R.drawable.icon_circle, R.color.redPrimary)
        }

        with(binding) {
            signalTypeText.text = signalText
            signalTypeIcon.setImageResource(signalIcon)
            signalTypeIcon.setColorFilter(ContextCompat.getColor(itemView.context, tintColor))
        }
    }
}
