package uz.prestige.livewater.dayver.route.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.level.route.types.BaseDataType
import uz.prestige.livewater.level.route.types.RouteType

class RouteAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<RouteType>(RouteDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, RouteDelegate(onClicked))
    }

    class RouteDiffutilsCallBack : DiffUtil.ItemCallback<RouteType>() {
        override fun areItemsTheSame(
            oldItem: RouteType,
            newItem: RouteType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RouteType,
            newItem: RouteType
        ): Boolean {
            return oldItem::class == newItem::class
        }


    }
}