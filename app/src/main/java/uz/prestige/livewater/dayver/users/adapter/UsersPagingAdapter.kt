package uz.prestige.livewater.dayver.users.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import uz.prestige.livewater.R
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.utils.inflate

class UsersPagingAdapter(private val onClicked: (position: Int) -> Unit) :
    PagingDataAdapter<DayverUserType, UsersPagingAdapter.UserViewHolder>(RouteDiffutilsCallBack()) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            parent.inflate(R.layout.item_users), onClicked
        )
    }

    class UserViewHolder(view: View, onClicked: (position: Int) -> Unit) :
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

    class RouteDiffutilsCallBack : DiffUtil.ItemCallback<DayverUserType>() {
        override fun areItemsTheSame(
            oldItem: DayverUserType,
            newItem: DayverUserType
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DayverUserType,
            newItem: DayverUserType
        ): Boolean {
            return oldItem == newItem
        }
    }
}