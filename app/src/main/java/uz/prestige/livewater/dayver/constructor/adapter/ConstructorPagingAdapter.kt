package uz.prestige.livewater.dayver.constructor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import uz.prestige.livewater.R
import uz.prestige.livewater.dayver.constructor.type.ConstructorType
import uz.prestige.livewater.utils.inflate

class ConstructorPagingAdapter() :
    PagingDataAdapter<ConstructorType, ConstructorPagingAdapter.LastUpdateViewHolder>(
        LastUpdateDiffutilsCallBack()
    ) {

    override fun onBindViewHolder(holder: LastUpdateViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LastUpdateViewHolder {
        return LastUpdateViewHolder(
            parent.inflate(R.layout.item_constructor)
        )
    }

    class LastUpdateViewHolder(view: View) : ConstructorBaseHolder(view) {
        fun bind(lastUpdate: ConstructorType) {
            bind(
                numbering = lastUpdate.numbering,
                serial = lastUpdate.serie.orEmpty(),
                level = lastUpdate.level.orEmpty(),
                volume = lastUpdate.volume.orEmpty(),
                time = lastUpdate.dateInMillisecond.orEmpty(),
                signal = lastUpdate.signal
            )
        }
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<ConstructorType>() {
        override fun areItemsTheSame(
            oldItem: ConstructorType,
            newItem: ConstructorType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ConstructorType,
            newItem: ConstructorType
        ): Boolean {
            return oldItem == newItem
        }
    }
}