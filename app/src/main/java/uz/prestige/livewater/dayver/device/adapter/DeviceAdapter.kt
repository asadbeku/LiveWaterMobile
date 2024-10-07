package uz.prestige.livewater.dayver.device.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.level.constructor.type.DeviceType

class DeviceAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<uz.prestige.livewater.dayver.constructor.type.DeviceType>(LastUpdateDiffutilsCallBack()) {

    var oldList: List<DeviceType> = listOf()

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    fun submitList(list: List<uz.prestige.livewater.dayver.constructor.type.DeviceType>) {
        items = list
    }

    init {
//        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, DevicePagingAdapter(onClicked))
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<uz.prestige.livewater.dayver.constructor.type.DeviceType>() {

        override fun areItemsTheSame(
            oldItem: uz.prestige.livewater.dayver.constructor.type.DeviceType,
            newItem: uz.prestige.livewater.dayver.constructor.type.DeviceType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: uz.prestige.livewater.dayver.constructor.type.DeviceType,
            newItem: uz.prestige.livewater.dayver.constructor.type.DeviceType
        ): Boolean {
            return oldItem == newItem
        }
    }
}