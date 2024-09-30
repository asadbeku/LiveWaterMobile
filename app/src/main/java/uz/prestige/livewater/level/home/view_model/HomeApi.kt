package uz.prestige.livewater.level.home.view_model

import retrofit2.Call
import retrofit2.http.GET

interface HomeApi {

    @GET("/regions")
    suspend fun getDevices(): Call<Any>

}