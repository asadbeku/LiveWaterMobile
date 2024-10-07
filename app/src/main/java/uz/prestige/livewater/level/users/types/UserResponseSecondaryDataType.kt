package uz.prestige.livewater.level.users.types


import com.google.gson.annotations.SerializedName

data class UserResponseSecondaryDataType(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("devices")
    val devices: List<Any?>,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("mobil_phone")
    val mobilPhone: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)