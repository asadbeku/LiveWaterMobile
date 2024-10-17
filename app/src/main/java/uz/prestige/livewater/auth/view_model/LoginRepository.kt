package uz.prestige.livewater.auth.view_model

import android.app.Application
import android.content.Context
import android.util.Base64
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.auth.types.AuthType
import uz.prestige.livewater.auth.types.DayverAuthSecondaryType
import uz.prestige.livewater.utils.UiState
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.getExpiredDateInMills
import uz.prestige.livewater.utils.getRoleFromJwt
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiServiceLevel: ApiService,
    private val apiServiceDayver: ApiServiceDayver,
    private val context: Context
) {

    fun authCheck(
        login: String,
        password: String,
        accountType: String
    ): Flow<UiState<String>> = flow {
        emit(UiState.Loading) // Indicate loading state

        try {
            val result = when (accountType) {
                "level" -> authenticate(
                    login,
                    password,
                    apiServiceLevel,
                    apiServiceDayver,
                    "level"
                )

                else -> authenticate(
                    login,
                    password,
                    apiServiceLevel,
                    apiServiceDayver,
                    "dayver"
                )
            }
            emit(UiState.Success(result)) // Emit success
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "An unknown error occurred")) // Emit error
        }
    }

    private suspend fun authenticate(
        login: String,
        password: String,
        apiService: ApiService,
        apiServiceDayver: ApiServiceDayver,
        profileType: String
    ): String {
        val response = if (profileType == "level") apiService.authCheck(
            login,
            password
        ) else apiServiceDayver.authCheck(login, password)
        return try {
            return if (response.isSuccessful && (response.code() == 200 || response.code() == 201)) {

                val token =
                    if (profileType == "level") {
                        val data = response as Response<AuthType>
                        data.body()!!.accsessToken
                    } else {
                        val data = response as Response<DayverAuthSecondaryType>
                        data.body()!!.token
                    }
//            val role = token.getRoleFromJwt()
                val role = if (profileType == "level") getRole(token) else getRoleDayver(token)
                val expTime = token.getExpiredDateInMills()

                Log.d("LoginViewModel", "authenticate - 1: $token, $role, $expTime")
                if (token.isNullOrEmpty() || role.isNullOrEmpty()) throw Exception("Token is null or empty")
                TokenManager.saveToken(context, token, expTime, role)
                Log.d("LoginViewModel", "authenticate - 2: $token")
                TokenManager.setProfileType(context, profileType)
                "Login successful"
            } else {
                handleAuthError(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun getRole(token: String): String {
        val payload = token.split(".")[1] // Extract JWT payload
        val decodedPayload = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
            .toString(Charsets.UTF_8) // Decode the payload to a string
        val json = JSONObject(decodedPayload) // Convert to JSON
        return json.getString("role") // Extract the role
    }

    private fun getRoleDayver(token: String): String? {
        // Check if token is null or empty
        if (token.isNullOrEmpty()) {
            return null // Return null or handle appropriately
        }

        return try {
            val jwtPayload = token.split(".")[1] // Extract the payload part of the JWT
            val decodedPayload = Base64.decode(jwtPayload, Base64.URL_SAFE or Base64.NO_WRAP)
                .toString(Charsets.UTF_8) // Decode the Base64-encoded payload
            val jsonObject = JSONObject(decodedPayload) // Convert decoded string to JSON
            val userObject = jsonObject.getJSONObject("user") // Get "user" object
            userObject.getString("role") // Extract "role" from the user object
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null or handle error
        }
    }

    private fun handleAuthError(response: Response<*>): Nothing {
        val errorMessage = when (response.code()) {
            401 -> "Incorrect login or password"
            400 -> "Password must be at least 8 characters"
            else -> "Authentication failed"
        }
        throw Exception(errorMessage)
    }

    suspend fun checkBearer(): Flow<UiState<ValidTokenResponse>> = flow {
        emit(UiState.Loading) // Loading state

        try {
            val responseLevel = apiServiceLevel.isValidToken()
            val responseDayver = apiServiceDayver.isValidToken()

            // Check Level token first
            if (responseLevel.isSuccessful && responseLevel.code() == 200) {
                emit(
                    UiState.Success(
                        ValidTokenResponse(
                            true,
                            "Valid token",
                            responseLevel.code()
                        )
                    )
                )
            } else if (responseDayver.isSuccessful && responseDayver.code() == 200) {
                emit(
                    UiState.Success(
                        ValidTokenResponse(
                            true,
                            "Valid token",
                            responseDayver.code()
                        )
                    )
                )
            } else {
                emit(UiState.Error("Invalid token"))
                TokenManager.clearToken(context)
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Token validation failed"))
            TokenManager.clearToken(context)
        }
    }
}

