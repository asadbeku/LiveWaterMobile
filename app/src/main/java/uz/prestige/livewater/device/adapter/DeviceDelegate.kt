package uz.prestige.livewater.device.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.constructor.type.DeviceType
import uz.prestige.livewater.utils.inflate

class DeviceDelegate(private val onClicked: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<DeviceType, DeviceType, DeviceDelegate.DeviceViewHolder>() {

    override fun isForViewType(
        p0: DeviceType,
        p1: MutableList<DeviceType>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): DeviceViewHolder {
        return DeviceViewHolder(
            p0.inflate(R.layout.item_device), onClicked = onClicked
        )
    }

    override fun onBindViewHolder(
        p0: DeviceType,
        p1: DeviceViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class DeviceViewHolder(view: View, onClicked: (position: Int) -> Unit) :
        DeviceBaseHolder(view, onClicked) {
        fun bind(device: DeviceType) {
            bind(
                numbering = device.numbering,
                serial = device.serialNumber,
                region = device.regionName,
                obyektNomi = device.objectName,
                owner = device.ownerName
            )
        }
    }
}