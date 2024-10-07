package uz.prestige.livewater.utils


import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)