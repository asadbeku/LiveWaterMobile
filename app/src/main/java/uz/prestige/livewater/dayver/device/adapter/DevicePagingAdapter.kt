package uz.prestige.livewater.dayver.device.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import uz.prestige.livewater.R
import uz.prestige.livewater.utils.inflate

class DevicePagingAdapter(private val onClicked: (position: Int) -> Unit) :
    PagingDataAdapter<uz.prestige.livewater.dayver.constructor.type.DeviceType, DevicePagingAdapter.DeviceViewHolder>(
        LastUpdateDiffutilsCallBack()
    ) {

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            parent.inflate(R.layout.item_device), onClicked
        )
    }

    class DeviceViewHolder(view: View, onClicked: (position: Int) -> Unit) :
        DeviceBaseHolder(view, onClicked) {
        fun bind(device: uz.prestige.livewater.dayver.constructor.type.DeviceType) {
            bind(
                numbering = device.numbering,
                serial = device.serialNumber,
                region = device.regionName,
                obyektNomi = device.objectName,
                owner = device.ownerName
            )
        }
    }

    class LastUpdateDiffutilsCallBack :
        DiffUtil.ItemCallback<uz.prestige.livewater.dayver.constructor.type.DeviceType>() {

        override fun areItemsTheSame(
            oldItem: uz.prestige.livewater.dayver.constructor.type.DeviceType,
            newItem: uz.prestige.livewater.dayver.constructor.type.DeviceType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: uz.prestige.livewater.dayver.constructor.type.DeviceType,
            newItem: uz.prestige.livewater.dayver.constructor.type.DeviceType
        ): Boolean {
            return oldItem == newItem
        }
    }
}