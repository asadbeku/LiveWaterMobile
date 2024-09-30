package uz.prestige.livewater.dayver.types.secondary


import com.google.gson.annotations.SerializedName

data class LastUpdateDayverSecondaryTypeItem(
    @SerializedName("date_in_ms")
    val dateInMs: Long,
    @SerializedName("device")
    val device: Device,
    @SerializedName("_id")
    val id: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("salinity")
    val salinity: Int,
    @SerializedName("signal")
    val signal: String,
    @SerializedName("temperature")
    val temperature: Int
)