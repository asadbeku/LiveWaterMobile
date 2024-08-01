package uz.prestige.livewater.users.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.users.types.UserType
import uz.prestige.livewater.utils.inflate

class UsersDelegate(private val onClicked: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<UserType, UserType, UsersDelegate.RouteViewHolder>() {

    override fun isForViewType(
        p0: UserType,
        p1: MutableList<UserType>,
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
        p0: UserType,
        p1: RouteViewHolder,
        p2: MutableList<Any>
    ) {
        p1.bind(p0)
    }

    class RouteViewHolder(view: View, onClicked: (position: Int) -> Unit) :
        UsersBaseHolder(view, onClicked) {

        fun bind(user: UserType) {
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