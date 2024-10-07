package uz.prestige.livewater.level.route.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import uz.prestige.livewater.R
import uz.prestige.livewater.level.route.types.RouteType
import uz.prestige.livewater.utils.inflate

class RoutePagingAdapter(private val onClicked: (position: Int) -> Unit) :
    PagingDataAdapter<RouteType, RoutePagingAdapter.RouteViewHolder>(RouteDiffutilsCallBack()) {

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(
            parent.inflate(R.layout.item_route), onClicked
        )
    }

    class RouteViewHolder(view: View, onClicked: (position: Int) -> Unit) :
        RouteBaseHolder(view, onClicked) {

        fun bind(routeItem: RouteType) {
            bind(
                privateKey = routeItem.privateKey,
                date = routeItem.date,
                statusCode = routeItem.statusCode,
                message = routeItem.message
            )
        }
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