package uz.prestige.livewater.level.users.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.convertToUserType
import javax.inject.Inject

class UsersRepository @Inject constructor(private val network: ApiService) {

    private val usersList: MutableList<String> = mutableListOf()

    fun saveUserId(id: String) = usersList.add(id)
    fun getUserIdByPosition(position: Int): String = usersList[position]

    fun getUserInfoById(id: String) = flow {
        val response = network.getLevelUserById(id)

        if (response.isSuccessful) {
            response.body()?.let { user ->
                emit(user.convertToUserType())
            }
        }
    }
}