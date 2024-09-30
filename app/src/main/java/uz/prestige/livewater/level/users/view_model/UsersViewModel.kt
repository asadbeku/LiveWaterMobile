package uz.prestige.livewater.level.users.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.users.types.UserType

class UsersViewModel : ViewModel() {

    private val repository = UsersRepository()

    private var privateUsersList: List<UserType> = listOf()

    val usersList: Flow<List<UserType>> = repository.usersList

    fun getUsers() {
        viewModelScope.launch {
            repository.getUsers()

            usersList.collectLatest {
                privateUsersList = it
            }
        }
    }

    fun getDeviceDataById(id: String): UserType? {
        return privateUsersList.firstOrNull() { it.id == id }
    }

    fun getDeviceId(position: Int): String {
        return privateUsersList.get(position).id
    }

}