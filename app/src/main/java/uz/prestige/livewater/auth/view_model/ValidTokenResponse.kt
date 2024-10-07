package uz.prestige.livewater.auth.view_model

data class ValidTokenResponse(
    val success: Boolean,
    val message: String,
    val statusCode: Int
)
