package uz.prestige.livewater.level.route.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.level.route.types.BaseDataType
import uz.prestige.livewater.level.route.types.RouteType
import uz.prestige.livewater.utils.inflate

class RouteDelegate(private val onClicked: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<RouteType, RouteType, RouteDelegate.RouteViewHolder>() {

    override fun isForViewType(
        p0: RouteType,
        p1: MutableList<RouteType>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): RouteViewHolder {
        return RouteViewHolder(
            p0.inflate(R.layout.item_route), onClicked
        )
    }

    override fun onBindViewHolder(
        p0: RouteType,
        p1: RouteViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class RouteViewHolder(view: View, onClicked: (position: Int) -> Unit) : RouteBaseHolder(view, onClicked) {

        fun bind(routeItem: RouteType) {
            bind(
                privateKey = routeItem.privateKey,
                date = routeItem.date,
                statusCode = routeItem.statusCode,
                message = routeItem.message
            )
        }
    }
}