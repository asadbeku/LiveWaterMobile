package uz.prestige.livewater.level.device.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.level.constructor.type.DeviceType

class DeviceAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<DeviceType>(LastUpdateDiffutilsCallBack()) {

    var oldList: List<DeviceType> = listOf()

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    fun submitList(list: List<DeviceType>) {
        items = list
    }

    init {
//        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, DevicePagingAdapter(onClicked))
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
            return oldItem == newItem
        }
    }
}