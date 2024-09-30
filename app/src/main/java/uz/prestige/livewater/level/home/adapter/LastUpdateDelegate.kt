package uz.prestige.livewater.level.home.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.utils.inflate

class LastUpdateDelegate :
    AbsListItemAdapterDelegate<LastUpdateType, LastUpdateType, uz.prestige.livewater.level.home.adapter.LastUpdateDelegate.LastUpdateViewHolder>() {

    override fun isForViewType(
        p0: LastUpdateType,
        p1: MutableList<LastUpdateType>,
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
        p0: LastUpdateType,
        p1: LastUpdateViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class LastUpdateViewHolder(view: View) : LastUpdateBaseHolder(view) {
        fun bind(lastUpdate: LastUpdateType) {
            bind(
                numbering = lastUpdate.numbering,
                serial = lastUpdate.serial.orEmpty(),
                level = lastUpdate.level.orEmpty(),
                volume = lastUpdate.volume.orEmpty(),
                salinity = lastUpdate.pressure.orEmpty(),
                time = lastUpdate.time.orEmpty(),
                signal = lastUpdate.signal,
                name = lastUpdate.name
            )
        }
    }
}