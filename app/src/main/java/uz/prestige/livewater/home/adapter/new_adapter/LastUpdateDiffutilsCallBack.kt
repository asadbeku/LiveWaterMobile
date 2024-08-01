package uz.prestige.livewater.home.adapter.new_adapter

import androidx.recyclerview.widget.DiffUtil
import uz.prestige.livewater.home.types.LastUpdateType

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