package uz.prestige.livewater.dayver.constructor.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.level.constructor.type.ConstructorType
import uz.prestige.livewater.utils.inflate

class ConstructorDelegate :
    AbsListItemAdapterDelegate<uz.prestige.livewater.dayver.constructor.type.ConstructorType, uz.prestige.livewater.dayver.constructor.type.ConstructorType, ConstructorDelegate.ConstructorViewHolder>() {

    override fun isForViewType(
        p0: uz.prestige.livewater.dayver.constructor.type.ConstructorType,
        p1: MutableList<uz.prestige.livewater.dayver.constructor.type.ConstructorType>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): ConstructorViewHolder {
        return ConstructorViewHolder(
            p0.inflate(R.layout.item_constructor)
        )
    }

    override fun onBindViewHolder(
        p0: uz.prestige.livewater.dayver.constructor.type.ConstructorType,
        p1: ConstructorViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class ConstructorViewHolder(view: View) : ConstructorBaseHolder(view) {
        fun bind(item: uz.prestige.livewater.dayver.constructor.type.ConstructorType) {
            bind(
                numbering = item.numbering,
                serial = item.serie,
                time = item.dateInMillisecond,
                signal = item.signal,
                level = item.level,
                volume = item.volume
            )
        }
    }
}