package uz.prestige.livewater.device.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.constructor.type.DeviceType

class DeviceAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<DeviceType>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, DeviceDelegate(onClicked))
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<DeviceType>() {
        override fun areItemsTheSame(
            oldItem: DeviceType,
            newItem: DeviceType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DeviceType,
            newItem: DeviceType
        ): Boolean {
            return oldItem::class == newItem::class
        }


    }
}