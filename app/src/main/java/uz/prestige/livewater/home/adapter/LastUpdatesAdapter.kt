package uz.prestige.livewater.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.home.types.LastUpdateType

class LastUpdatesAdapter :
    AsyncListDifferDelegationAdapter<LastUpdateType>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, LastUpdateDelegate())
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
            return oldItem::class == newItem::class
        }


    }
}