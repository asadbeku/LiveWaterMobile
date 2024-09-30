package uz.prestige.livewater.dayver.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.dayver.types.LastUpdateTypeDayver

class LastUpdatesAdapterDayver :
    AsyncListDifferDelegationAdapter<LastUpdateTypeDayver>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, LastUpdateDelegateDayver())
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<LastUpdateTypeDayver>() {
        override fun areItemsTheSame(
            oldItem: LastUpdateTypeDayver,
            newItem: LastUpdateTypeDayver
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LastUpdateTypeDayver,
            newItem: LastUpdateTypeDayver
        ): Boolean {
            return oldItem == newItem
        }


    }
}