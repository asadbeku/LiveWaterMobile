package uz.prestige.livewater.level.device.type


import com.google.gson.annotations.SerializedName

data class DeviceResponseSecondaryType(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("device_privet_key")
    val devicePrivetKey: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("isWorking")
    val isWorking: Boolean,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double,
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