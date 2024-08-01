package uz.prestige.livewater.login.view_model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import uz.prestige.livewater.login.TokenManager
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network
import uz.prestige.livewater.utils.getExpiredDateInMills
import uz.prestige.livewater.utils.getRoleFromJwt

class LoginRepository {

    suspend fun authCheck(context: Context, login: String, password: String): Flow<Boolean> = flow {
        val response = Network.buildService(ApiService::class.java).authCheck(login, password)

        if (response.isSuccessful && (response.code()==200 || response.code() == 201)) {
            val jsonBody = Gson().toJson(response.body())
            val jsonObject = JSONObject(jsonBody)
            val token = jsonObject.optString("token")

            TokenManager.saveToken(context, token, token.getExpiredDateInMills(), token.getRoleFromJwt())

            if (TokenManager.getToken(context).isNullOrBlank()) emit(false) else emit(true)
        } else {
            Throwable("Login yoki parol xato")
            emit(false)
        }
    }


    suspend fun checkBearer() = flow {

        val response = Network.buildService(ApiService::class.java).isValidToken()

        Log.d("checkBearer", "Network: ${response.body()}")

        if (response.isSuccessful && response.code() == 200) emit(true) else emit(false)
    }


}