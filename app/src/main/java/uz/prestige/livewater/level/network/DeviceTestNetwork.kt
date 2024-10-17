package uz.prestige.livewater.level.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeviceTestNetwork {
    private const val BASE_URL = "http://livewater.uz:1880/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}