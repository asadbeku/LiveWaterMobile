package uz.prestige.livewater.level.device.type


import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("_id")
    val id: String,
    @SerializedName("id")
    val id1: String,
    @SerializedName("name")
    val name: String
)