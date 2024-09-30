package uz.prestige.livewater.dayver.device.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.databinding.ItemDeviceBinding

abstract class DeviceBaseHolder(view: View, onClicked: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemDeviceBinding.bind(view)

    init {
        view.setOnClickListener {
            onClicked(absoluteAdapterPosition)
        }
    }

    fun bind(
        numbering: Int,
        serial: String,
        region: String,
        obyektNomi: String,
        owner: String
    ) {
        binding.numbering.text = numbering.toString()
        binding.serialNumber.text = serial
        binding.region.text = region
        binding.obyektNomi.text = obyektNomi
        binding.owner.text = owner

//        setupSignalTypeIcon(signal)
    }

//    private fun setupSignalTypeIcon(signal: Boolean) {
//        val signalText = if (signal) "Yaxshi" else "Signal yo'q"
//        val signalIcon = if (signal) R.drawable.icon_circle else R.drawable.icon_circle
//
//        binding.statusText.text = signalText
//        binding.statusCircle.setImageResource(signalIcon)
//
//        val tintColor = ContextCompat.getColor(
//            itemView.context,
//            if (signal) R.color.greenPrimary else R.color.redPrimary
//        )
//        binding.statusCircle.setColorFilter(tintColor)
//    }

}