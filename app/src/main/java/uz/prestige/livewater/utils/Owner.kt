package uz.prestige.livewater.utils


import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("username")
    val username: String
)