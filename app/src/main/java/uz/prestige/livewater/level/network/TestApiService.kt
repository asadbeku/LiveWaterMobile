package uz.prestige.livewater.level.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TestApiService {

    @GET("/{serialNumber}")
    suspend fun makeRequestToDevice(@Path("serialNumber") serialNumber: String): Response<Any>

}