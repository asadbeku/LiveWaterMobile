package uz.prestige.livewater.dayver.route.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemRouteBinding
import uz.prestige.livewater.level.route.types.BaseDataType
import uz.prestige.livewater.utils.toFormattedDate
import uz.prestige.livewater.utils.toFormattedTime

abstract class RouteBaseHolder(view: View, private val onClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(view) {

    private val binding = ItemRouteBinding.bind(view)

    init {
        view.setOnClickListener {
            onClicked(absoluteAdapterPosition)
        }
    }

    fun bind(date: Long, privateKey: String, statusCode: Int, message: String) {
        binding.statusCode.text = statusCode.toString()
        setupSignalTypeIcon(statusCode, message)

        binding.time.text = date.toFormattedTime()
        binding.date.text = date.toFormattedDate()

        binding.privateKey.text = privateKey
    }

    private fun setupSignalTypeIcon(statusCode: Int, message: String) {
        val signalIcon = R.drawable.icon_circle

        binding.statusCode.text = "Status code: $statusCode"
        binding.statusCircle.setImageResource(signalIcon)

        val tintColor = ContextCompat.getColor(
            itemView.context,
            if (statusCode == 200) R.color.greenPrimary else R.color.redPrimary
        )

        binding.statusCircle.setColorFilter(tintColor)

        binding.message.setTextColor(tintColor)
        binding.message.text = message
    }

}