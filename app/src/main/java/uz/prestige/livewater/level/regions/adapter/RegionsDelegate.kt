package uz.prestige.livewater.level.regions.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.utils.inflate

class RegionsDelegate(
    private val buttonClickListener: ButtonClickListener
) :
    AbsListItemAdapterDelegate<RegionType, RegionType, RegionsDelegate.RegionsViewHolder>() {

    override fun isForViewType(
        p0: RegionType,
        p1: MutableList<RegionType>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): RegionsViewHolder {
        return RegionsViewHolder(
            p0.inflate(R.layout.item_regions), buttonClickListener
        )
    }

    override fun onBindViewHolder(
        p0: RegionType,
        p1: RegionsViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class RegionsViewHolder(
        view: View, buttonClickListener: ButtonClickListener
    ) :
        RegionsBaseHolder(view, buttonClickListener) {
        fun bind(region: RegionType) {
            bind(region.numbering, region.name, region.deviceCount)
        }
    }
}