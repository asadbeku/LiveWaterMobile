package uz.prestige.livewater.level.users.view_model

import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.convertRegionSecondaryToRegionType
import javax.inject.Inject

class AddUserRepository @Inject constructor(
    private val apiService: ApiService
) {

    private val _regionsList = MutableStateFlow<List<RegionType>>(emptyList())
    val regionsList: StateFlow<List<RegionType>> = _regionsList

    suspend fun getRegionsFromNetwork() {

        val response = apiService.getRegions()

        if (response.isSuccessful) {

            val list = response.body()?.convertRegionSecondaryToRegionType()

            list?.let { _regionsList.emit(it) }
        }

    }

    suspend fun addUser(json: JsonObject) = flow {

        val response = apiService.addUser(json)

        if (response.isSuccessful && response.code() == 200 || response.code() == 201) emit(true) else emit(
            false
        )
    }

    suspend fun changeUser(id: String, userJson: JsonObject) = flow {
        try {
            val response = apiService.updateUser(id, userJson)

            if (response.isSuccessful && response.code() == 200 || response.code() == 201) emit(true) else emit(
                false
            )
        } catch (e: java.lang.Exception) {
            Log.e("addUserCheck", "Error adding user: ${e.message}")
        }
    }

    fun removeUser(id: String) = flow<Boolean> {
        val response = apiService.deleteUser(id)

        if (response.isSuccessful && response.code() == 200 || response.code() == 201) emit(true) else emit(
            false
        )
    }

}