package uz.prestige.livewater.level.users.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.utils.convertToUserType

class UsersRepository {

    private val usersList: MutableList<String> = mutableListOf()

    suspend fun getUsers() {
//        val response =
//            NetworkLevel.buildService(ApiService::class.java).getLevelUsers()

//        if (response.isSuccessful) {
//            val usersList = response.body()?.convertToUserType().orEmpty()
//
////            _usersList.emit(usersList)
//        }


    }

    fun saveUserId(id: String) = usersList.add(id)
    fun getUserIdByPosition(position: Int): String = usersList[position]

    fun getUserInfoById(id: String) = flow {
        val response = NetworkLevel.buildService(ApiService::class.java).getLevelUserById(id)

        if (response.isSuccessful) {
            response.body()?.let { user ->
                emit(user.convertToUserType())
            }
        }
    }
}