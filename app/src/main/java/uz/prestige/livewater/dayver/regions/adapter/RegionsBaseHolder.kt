package uz.prestige.livewater.dayver.regions.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemLastInfoBinding
import uz.prestige.livewater.databinding.ItemRegionsBinding
import uz.prestige.livewater.utils.toFormattedTime
import kotlin.math.abs

abstract class RegionsBaseHolder(
    view: View, private val buttonClickListener: ButtonClickListener
) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemRegionsBinding.bind(view)

    init {
        binding.editButton.setOnClickListener {
            buttonClickListener.onEditButtonClick(absoluteAdapterPosition)
        }
        binding.removeButton.setOnClickListener {
            buttonClickListener.onRemoveButtonClick(absoluteAdapterPosition)
        }
    }

    fun bind(numbering: Int, regionsName: String, deviceCount: Int) {
        binding.numbering.text = numbering.toString()
        binding.regionName.text = regionsName
        binding.deviceCount.text = deviceCount.toString()
    }

}