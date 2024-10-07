package uz.prestige.livewater.dayver.constructor.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ConstructorAdapter :
    AsyncListDifferDelegationAdapter<uz.prestige.livewater.dayver.constructor.type.ConstructorType>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, ConstructorDelegate())
    }

    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<uz.prestige.livewater.dayver.constructor.type.ConstructorType>() {
        override fun areItemsTheSame(
            oldItem: uz.prestige.livewater.dayver.constructor.type.ConstructorType,
            newItem: uz.prestige.livewater.dayver.constructor.type.ConstructorType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: uz.prestige.livewater.dayver.constructor.type.ConstructorType,
            newItem: uz.prestige.livewater.dayver.constructor.type.ConstructorType
        ): Boolean {
            return oldItem::class == newItem::class
        }


    }
}