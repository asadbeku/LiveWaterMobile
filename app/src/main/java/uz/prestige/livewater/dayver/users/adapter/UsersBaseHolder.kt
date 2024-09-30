package uz.prestige.livewater.dayver.users.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ItemRouteBinding
import uz.prestige.livewater.databinding.ItemUsersBinding
import uz.prestige.livewater.utils.toFormattedDate
import uz.prestige.livewater.utils.toFormattedTime

abstract class UsersBaseHolder(view: View, private val onClicked: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemUsersBinding.bind(view)

    init {
        view.setOnClickListener {
            onClicked(absoluteAdapterPosition)
        }
    }

    fun bind(
        numbering: Int,
        firsName: String,
        lastName: String,
        username: String,
        role: String,
        deviceCount: Int,
        createdAt: Long,
        updatedAt: Long
    ) {
        binding.username.text = username
        binding.name.text = firsName
        binding.surname.text = lastName
        binding.numbering.text = numbering.toString()
        binding.role.text = role
        binding.deviceCount.text = deviceCount.toString()
        binding.createdAt.text = "${createdAt.toFormattedTime()} || ${createdAt.toFormattedDate()}"
        binding.updatedAt.text = "${updatedAt.toFormattedTime()} || ${updatedAt.toFormattedDate()}"
    }

}