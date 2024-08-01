package uz.prestige.livewater.home.types.test


import com.google.gson.annotations.SerializedName

data class LastUpdateSecondaryTypeItem(
    @SerializedName("date_in_ms")
    val dateInMs: Long,
    @SerializedName("device")
    val device: DeviceX,
    @SerializedName("_id")
    val id: String,
    @SerializedName("level")
    val level: Double,
    @SerializedName("signal")
    val signal: String,
    @SerializedName("volume")
    val volume: String,
)