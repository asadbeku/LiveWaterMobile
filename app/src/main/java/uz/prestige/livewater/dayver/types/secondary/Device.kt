package uz.prestige.livewater.dayver.types.secondary


import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("serie")
    val serie: String
)