package uz.prestige.livewater.dayver.users.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import uz.prestige.livewater.dayver.users.types.DayverUserType

class UsersAdapter(onClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<DayverUserType>(RouteDiffutilsCallBack()) {

    companion object {
        private const val LAST_UPDATE_DELEGATE_ID = 1
    }

    init {
        delegatesManager.addDelegate(LAST_UPDATE_DELEGATE_ID, UsersDelegate(onClicked))
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
            return oldItem== newItem
        }


    }
}