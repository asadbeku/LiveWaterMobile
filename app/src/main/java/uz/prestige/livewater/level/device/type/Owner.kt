package uz.prestige.livewater.level.device.type


import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("id")
    val id1: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("username")
    val username: String
)