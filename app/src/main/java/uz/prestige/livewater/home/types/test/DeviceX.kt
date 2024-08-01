package uz.prestige.livewater.home.types.test


import com.google.gson.annotations.SerializedName

data class DeviceX(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("serie")
    val serie: String,
)