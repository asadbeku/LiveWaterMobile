package uz.prestige.livewater.dayver.users.view_model

import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.utils.convertToUserType
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiService: ApiServiceDayver
) {
    private val userList = mutableListOf<String>()

    fun saveUserId(id: String) = userList.add(id)

    fun getUserId(position: Int): String = userList[position]
    fun getUserDataById(userId: String) = flow {
        val response = apiService.getDayverUserById(userId)

        if (response.isSuccessful) {
            emit(response.body()!!.convertToUserType())
        }

    }
}