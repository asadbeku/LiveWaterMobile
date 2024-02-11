package uz.prestige.livewater.constructor.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.constructor.type.ConstructorType

class ConstructorAdapter :
    AsyncListDifferDelegationAdapter<ConstructorType>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, ConstructorDelegate())
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
            return oldItem::class == newItem::class
        }


    }
}