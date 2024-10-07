package uz.prestige.livewater.dayver.constructor.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemConstructorBinding
import uz.prestige.livewater.utils.toFormattedDate
import uz.prestige.livewater.utils.toFormattedTime

abstract class ConstructorBaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemConstructorBinding.bind(view)

    init {
        view.setOnClickListener {

        }
    }

    fun bind(
        numbering: String,
        serial: String,
        signal: Boolean,
        time: String,
        level: String,
        volume: String
    ) {
        binding.numberTextView.text = numbering
        binding.serieTextView.text = serial
        binding.waterLevelTextView.text = level
        binding.timeTextView.text =
            "${time.toLong().toFormattedTime()} || ${time.toLong().toFormattedDate()}"
        binding.waterVolumeTextView.text = volume

        setupSignalTypeIcon(signal)
    }

    private fun setupSignalTypeIcon(signal: Boolean) {
        val signalText = if (signal) "Yaxshi" else "Signal yo'q"
        val signalIcon = if (signal) R.drawable.icon_circle else R.drawable.icon_circle

        binding.signalTextView.text = signalText
        binding.signalImageView.setImageResource(signalIcon)

        val tintColor = ContextCompat.getColor(
            itemView.context,
            if (signal) R.color.greenPrimary else R.color.redPrimary
        )
        binding.signalImageView.setColorFilter(tintColor)
    }

}