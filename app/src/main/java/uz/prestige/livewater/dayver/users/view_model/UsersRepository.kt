package uz.prestige.livewater.dayver.users.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertToUserType

class UsersRepository {

    private val _usersList = MutableStateFlow<List<DayverUserType>>(emptyList())
    val usersList: StateFlow<List<DayverUserType>> = _usersList

    suspend fun getUsers() {
        val response =
            NetworkDayver.buildService(ApiService::class.java).getDayverUsers()

        if (response.isSuccessful) {
            val usersList = response.body()?.convertToUserType().orEmpty()

            _usersList.emit(usersList)
        }


    }
}