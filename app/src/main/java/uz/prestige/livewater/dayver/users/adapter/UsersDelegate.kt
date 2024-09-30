package uz.prestige.livewater.dayver.users.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.utils.inflate

class UsersDelegate(private val onClicked: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<DayverUserType, DayverUserType, UsersDelegate.RouteViewHolder>() {

    override fun isForViewType(
        p0: DayverUserType,
        p1: MutableList<DayverUserType>,
        p2: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(p0: ViewGroup): RouteViewHolder {
        return RouteViewHolder(
            p0.inflate(R.layout.item_users), onClicked
        )
    }

    override fun onBindViewHolder(
        p0: DayverUserType,
        p1: RouteViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class RouteViewHolder(view: View, onClicked: (position: Int) -> Unit) :
        UsersBaseHolder(view, onClicked) {

        fun bind(user: DayverUserType) {
            bind(
                numbering = user.numbering,
                firsName = user.firstName,
                lastName = user.lastName,
                role = user.role,
                username = user.username,
                deviceCount = user.devicesCount,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}