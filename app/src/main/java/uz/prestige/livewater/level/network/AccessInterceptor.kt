package uz.prestige.livewater.level.network

import okhttp3.Interceptor
import okhttp3.Response

class AccessInterceptor(private val bearer: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $bearer")
            .build()
        return chain.proceed(request)
    }
}