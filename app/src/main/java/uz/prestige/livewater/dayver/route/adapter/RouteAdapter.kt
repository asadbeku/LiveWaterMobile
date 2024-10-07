package uz.prestige.livewater.dayver.route.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.dayver.route.types.RouteType

class RouteAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<RouteType>(RouteDiffutilsCallBack()) {

    companion object {
        private const val ROUTE_DELEGATE_ID = 1 // Changed for clarity
    }

    init {
//        delegatesManager.addDelegate(ROUTE_DELEGATE_ID, RoutePagingAdapter(onClicked))
    }

    class RouteDiffutilsCallBack : DiffUtil.ItemCallback<RouteType>() {
        override fun areItemsTheSame(
            oldItem: RouteType,
            newItem: RouteType
        ): Boolean {
            // Compare unique identifiers for items
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RouteType,
            newItem: RouteType
        ): Boolean {
            // Compare relevant fields for content equality
            return oldItem == newItem // Ensure RouteType has proper equals() implementation
        }
    }
}
