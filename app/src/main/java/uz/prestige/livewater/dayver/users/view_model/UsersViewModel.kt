package uz.prestige.livewater.dayver.users.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.prestige.livewater.dayver.users.types.DayverUserType

class UsersViewModel : ViewModel() {

    private val repository = UsersRepository()

    private var privateUsersList: List<DayverUserType> = listOf()

    val usersList: Flow<List<DayverUserType>> = repository.usersList

    fun getUsers() {
        viewModelScope.launch {
            repository.getUsers()

            usersList.collectLatest {
                privateUsersList = it
            }
        }
    }

    fun getDeviceDataById(id: String): DayverUserType? {
        return privateUsersList.firstOrNull() { it.id == id }
    }

    fun getDeviceId(position: Int): String {
        Log.d("userInfo", "getDeviceId: ${privateUsersList[position].id}")
        return privateUsersList[position].id
    }

}