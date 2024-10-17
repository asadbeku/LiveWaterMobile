package uz.prestige.livewater.auth.types


import com.google.gson.annotations.SerializedName

data class DayverAuthSecondaryType(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("token")
    val token: String
)