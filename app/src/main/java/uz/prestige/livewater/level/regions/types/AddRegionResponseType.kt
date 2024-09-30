package uz.prestige.livewater.level.regions.types


import com.google.gson.annotations.SerializedName

data class AddRegionResponseType(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)