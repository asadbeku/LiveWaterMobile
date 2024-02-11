package uz.prestige.livewater.login.view_model

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network

class LoginRepository {

    suspend fun authCheck(login: String, password: String): Flow<Boolean> = flow {
        val response = Network.buildService(ApiService::class.java).authCheck(login, password)

        if (response.isSuccessful) {
            val jsonBody = Gson().toJson(response.body())
            val jsonObject = JSONObject(jsonBody)
            val token = jsonObject.optString("token")

            Log.d("LoginRepository", "authCheck: $token")

            emit(true)
        } else {
            emit(false)
        }
    }


}