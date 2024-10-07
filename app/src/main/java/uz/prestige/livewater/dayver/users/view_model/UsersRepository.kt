package uz.prestige.livewater.dayver.users.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertToUserType

class UsersRepository {

    private val userList = mutableListOf<String>()

    fun saveUserId(id: String) = userList.add(id)

    fun getUserId(position: Int): String = userList[position]
    fun getUserDataById(userId: String) = flow<DayverUserType>{
        val response = NetworkDayver.buildService(ApiService::class.java).getDayverUserById(userId)

        if(response.isSuccessful){
            emit(response.body()!!.convertToUserType())
        }

    }
}