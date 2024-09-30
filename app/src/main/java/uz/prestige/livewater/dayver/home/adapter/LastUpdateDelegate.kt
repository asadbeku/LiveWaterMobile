package uz.prestige.livewater.dayver.home.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.dayver.types.LastUpdateTypeDayver
import uz.prestige.livewater.level.home.adapter.LastUpdateDelegate
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.utils.inflate

class LastUpdateDelegateDayver :
    AbsListItemAdapterDelegate<LastUpdateTypeDayver, LastUpdateTypeDayver, LastUpdateDelegateDayver.DayverLastUpdateViewHolder>() {

    override fun isForViewType(
        p0: LastUpdateTypeDayver,
        p1: MutableList<LastUpdateTypeDayver>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): DayverLastUpdateViewHolder {
        return DayverLastUpdateViewHolder(
            p0.inflate(R.layout.item_last_info_dayver)
        )
    }

    override fun onBindViewHolder(
        item: LastUpdateTypeDayver,
        holder: DayverLastUpdateViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class DayverLastUpdateViewHolder(view: View) : DayverLastUpdateBaseHolder(view) {
        fun bind(lastUpdate: LastUpdateTypeDayver) {
            bind(
                numbering = lastUpdate.numbering,
                serial = lastUpdate.serial.orEmpty(),
                level = lastUpdate.level.orEmpty(),
                salinity = lastUpdate.salinity.orEmpty(),
                temperature = lastUpdate.temperature.orEmpty(),
                signal = lastUpdate.signal,
                time = lastUpdate.time.orEmpty(),
                name = lastUpdate.name
            )
        }
    }
}