package uz.prestige.livewater.users.view_model

import android.content.Context
import com.squareup.moshi.Json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uz.prestige.livewater.login.TokenManager
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.users.types.UserType
import uz.prestige.livewater.utils.convertToUserType

class UsersRepository {

    private val _usersList = MutableStateFlow<List<UserType>>(emptyList())
    val usersList: StateFlow<List<UserType>> = _usersList

    suspend fun getUsers() {
        val response =
            Network.buildService(ApiService::class.java).getUsers()

        if (response.isSuccessful) {
            val usersList = response.body()?.convertToUserType().orEmpty()

            _usersList.emit(usersList)
        }


    }
}