package uz.prestige.livewater.home.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.home.types.DeviceLastUpdate
import uz.prestige.livewater.utils.inflate

class LastUpdateDelegate :
    AbsListItemAdapterDelegate<DeviceLastUpdate, DeviceLastUpdate, LastUpdateDelegate.LastUpdateViewHolder>() {

    override fun isForViewType(
        p0: DeviceLastUpdate,
        p1: MutableList<DeviceLastUpdate>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): LastUpdateViewHolder {
        return LastUpdateViewHolder(
            p0.inflate(R.layout.item_last_info)
        )
    }

    override fun onBindViewHolder(
        p0: DeviceLastUpdate,
        p1: LastUpdateViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class LastUpdateViewHolder(view: View) : LastUpdateBaseHolder(view) {
        fun bind(lastUpdate: DeviceLastUpdate) {
            bind(
                numbering = lastUpdate.numbering ?: 0,
                serial = lastUpdate.serial.orEmpty(),
                level = lastUpdate.level.orEmpty(),
                volume = lastUpdate.volume.orEmpty(),
                salinity = lastUpdate.salinity.orEmpty(),
                time = lastUpdate.time.orEmpty(),
                signal = lastUpdate.signal
            )
        }
    }
}