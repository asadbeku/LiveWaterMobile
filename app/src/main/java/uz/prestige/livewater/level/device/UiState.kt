package uz.prestige.livewater.level.device

sealed class UiState {
    object None : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}