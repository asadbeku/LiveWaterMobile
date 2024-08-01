package uz.prestige.livewater.users.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.route.types.RouteType
import uz.prestige.livewater.users.types.UserType

class UsersAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<UserType>(RouteDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, UsersDelegate(onClicked))
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
            return oldItem== newItem
        }


    }
}