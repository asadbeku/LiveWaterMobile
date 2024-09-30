package uz.prestige.livewater.level.users.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.utils.convertToUserType

class UsersRepository {

    private val _usersList = MutableStateFlow<List<UserType>>(emptyList())
    val usersList: StateFlow<List<UserType>> = _usersList

    suspend fun getUsers() {
        val response =
            NetworkLevel.buildService(ApiService::class.java).getLevelUsers()

        if (response.isSuccessful) {
            val usersList = response.body()?.convertToUserType().orEmpty()

            _usersList.emit(usersList)
        }


    }
}