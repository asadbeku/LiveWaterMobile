package uz.prestige.livewater.level.users.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import uz.prestige.livewater.R
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.utils.inflate

class UsersPagingAdapter(private val onClicked: (position: Int) -> Unit) :
    PagingDataAdapter<UserType, UsersPagingAdapter.RouteViewHolder>(RouteDiffutilsCallBack()) {


    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(
            parent.inflate(R.layout.item_users), onClicked
        )
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

    class RouteDiffutilsCallBack : DiffUtil.ItemCallback<UserType>() {
        override fun areItemsTheSame(
            oldItem: UserType,
            newItem: UserType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserType,
            newItem: UserType
        ): Boolean {
            return oldItem == newItem
        }
    }
}