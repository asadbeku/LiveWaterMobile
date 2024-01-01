package uz.prestige.livewater.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.home.types.DeviceLastUpdate

class LastUpdatesAdapter :
    AsyncListDifferDelegationAdapter<DeviceLastUpdate>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, LastUpdateDelegate())
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<DeviceLastUpdate>() {
        override fun areItemsTheSame(
            oldItem: DeviceLastUpdate,
            newItem: DeviceLastUpdate
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DeviceLastUpdate,
            newItem: DeviceLastUpdate
        ): Boolean {
            return oldItem::class == newItem::class
        }


    }
}