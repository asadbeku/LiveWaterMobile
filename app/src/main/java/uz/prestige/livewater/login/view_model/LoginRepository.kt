package uz.prestige.livewater.login.view_model

import kotlinx.coroutines.flow.flow

class LoginRepository {

    private val loginServer = "Asadbekman"
    private val passwordServer = "20010827Aa"

    suspend fun authCheck(login: String, password: String) = flow {

        if (login == loginServer && password == passwordServer) {
            emit(true)
        } else {
            emit(false)
        }
    }

}