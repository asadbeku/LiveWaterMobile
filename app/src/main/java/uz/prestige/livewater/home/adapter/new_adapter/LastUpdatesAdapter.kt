package uz.prestige.livewater.home.adapter.new_adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import uz.prestige.livewater.R
import uz.prestige.livewater.home.adapter.LastUpdateBaseHolder
import uz.prestige.livewater.home.types.LastUpdateType
import uz.prestige.livewater.utils.inflate

class LastUpdatePagingAdapter() :
    PagingDataAdapter<LastUpdateType, LastUpdatePagingAdapter.LastUpdateViewHolder>(LastUpdateDiffutilsCallBack()) {

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
                name=lastUpdate.name
            )
        }
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<LastUpdateType>() {
        override fun areItemsTheSame(
            oldItem: LastUpdateType,
            newItem: LastUpdateType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LastUpdateType,
            newItem: LastUpdateType
        ): Boolean {
            return oldItem == newItem
        }


    }

    override fun onBindViewHolder(holder: LastUpdateViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LastUpdateViewHolder {
        return LastUpdateViewHolder(
            parent.inflate(R.layout.item_last_info)
        )
    }
}