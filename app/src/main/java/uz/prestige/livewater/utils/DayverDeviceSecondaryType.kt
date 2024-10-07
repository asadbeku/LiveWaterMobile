package uz.prestige.livewater.utils


import com.google.gson.annotations.SerializedName

data class DayverDeviceSecondaryType(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("device_privet_key")
    val devicePrivetKey: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("lat")
    val lat: Int,
    @SerializedName("long")
    val long: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("region")
    val region: Region,
    @SerializedName("serie")
    val serie: String,
    @SerializedName("updated_at")
    val updatedAt: String
)