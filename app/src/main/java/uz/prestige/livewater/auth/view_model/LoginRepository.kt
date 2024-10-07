package uz.prestige.livewater.auth.view_model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.getExpiredDateInMills
import uz.prestige.livewater.utils.getRoleFromJwt

class LoginRepository {

    fun authCheck(
        context: Context,
        login: String,
        password: String,
        accountType: String
    ): Flow<String> = flow {
        emit(
            when (accountType) {
                "level" -> authLevel(context, login, password)
                else -> authDayver(context, login, password)
            }
        )
    }

    private suspend fun authLevel(context: Context, login: String, password: String): String {
        val response = NetworkLevel.buildService(ApiService::class.java).authCheck(login, password)

        return when {
            response.isSuccessful && (response.code() == 200 || response.code() == 201) -> {
                val jsonBody = Gson().toJson(response.body())
                val jsonObject = JSONObject(jsonBody)
                val token = jsonObject.optString("token")

                TokenManager.saveToken(
                    context,
                    token,
                    token.getExpiredDateInMills(),
                    token.getRoleFromJwt()
                )
                TokenManager.setProfileType(context, "level")

                if (TokenManager.getToken(context)?.isNotEmpty() == true) {
                    "Login"
                } else {
                    "unsuccessful"
                }
            }

            response.code() == 401 -> throw Throwable("Login yoki parol xato")
            response.code() == 400 -> throw Throwable("Parol 8 ta simvoldan kam")
            else -> "unsuccessful"
        }
    }

    private suspend fun authDayver(context: Context, login: String, password: String): String {
        val response = NetworkDayver.buildService(ApiService::class.java).authCheck(login, password)

        return when {
            response.isSuccessful && (response.code() == 200 || response.code() == 201) -> {
                val jsonBody = Gson().toJson(response.body())
                val jsonObject = JSONObject(jsonBody)
                val token = jsonObject.optString("token")

                TokenManager.saveToken(
                    context,
                    token,
                    token.getExpiredDateInMills(),
                    token.getRoleFromJwt()
                )
                TokenManager.setProfileType(context, "dayver")

                if (TokenManager.getToken(context)?.isNotEmpty() == true) {
                    "Login"
                } else {
                    "unsuccessful"
                }
            }

            response.code() == 401 -> {
                throw Throwable("Login yoki parol xato")
            }

            response.code() == 400 -> {
                throw Throwable("Parol 8 ta simvoldan kam")
            }

            else -> "unsuccessful"
        }
    }

    suspend fun checkBearer(context: Context) = flow {

        val responseDayver = NetworkDayver.buildService(ApiService::class.java).isValidToken()

        val response = NetworkLevel.buildService(ApiService::class.java).isValidToken()

        if (responseDayver.isSuccessful && responseDayver.code() == 200) {
            emit(ValidTokenResponse(responseDayver.isSuccessful, "Success bearer", responseDayver.code()))
        } else if (response.isSuccessful && response.code() == 200) {
            emit(ValidTokenResponse(response.isSuccessful, "Success bearer", response.code()))
        } else {
            emit(ValidTokenResponse(false, "unsuccessful", response.code()))
            TokenManager.clearToken(context)
        }

        Log.d("checkBearer", "Network: ${response.body()}")
    }
}
