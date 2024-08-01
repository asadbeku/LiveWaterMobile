package uz.prestige.livewater.device

sealed class UiState {
    object None : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}