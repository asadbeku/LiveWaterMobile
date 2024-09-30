package uz.prestige.livewater.level.regions.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.level.constructor.type.RegionType

class RegionsAdapter(private val buttonClickListener: ButtonClickListener) :
    AsyncListDifferDelegationAdapter<RegionType>(LastUpdateDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, RegionsDelegate(buttonClickListener))
    }

    fun submitList(list: List<RegionType>) {
        items = list
    }


    class LastUpdateDiffutilsCallBack : DiffUtil.ItemCallback<RegionType>() {
        override fun areItemsTheSame(
            oldItem: RegionType,
            newItem: RegionType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RegionType,
            newItem: RegionType
        ): Boolean {
            return oldItem == newItem
        }
    }
}