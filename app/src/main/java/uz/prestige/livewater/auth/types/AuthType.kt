package uz.prestige.livewater.auth.types


import com.google.gson.annotations.SerializedName

data class AuthType(
    @SerializedName("accsessToken")
    val accsessToken: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)