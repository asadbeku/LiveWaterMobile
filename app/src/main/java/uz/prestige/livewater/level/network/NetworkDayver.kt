package uz.prestige.livewater.level.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.prestige.livewater.auth.TokenManager
import java.lang.ref.WeakReference

object NetworkDayver {
    private var contextRef: WeakReference<Context>? = null

    // Method to set the context
    fun setContext(context: Context) {
        contextRef = WeakReference(context)
    }

    // Method to retrieve the context if available
    private fun getContext(): Context? {
        return contextRef?.get()
    }

    // Use the context if available
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val context = getContext()
            val isValidToken = context?.let { TokenManager.isTokenValid(it) }

            val request = if (isValidToken == true) {
                val token = context.let { TokenManager.getToken(it!!) }
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .method(original.method, original.body)
                requestBuilder.build()
            } else {
                null
            }

            chain.proceed(request ?: original)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://back1.livewater.uz/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}