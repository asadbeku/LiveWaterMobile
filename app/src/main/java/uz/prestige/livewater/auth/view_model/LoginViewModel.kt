package uz.prestige.livewater.auth.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.utils.UiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean> get() = _updatingState

    private val _status = MutableLiveData<UiState<*>>()
    val status: LiveData<UiState<*>> get() = _status

    fun checkAuth(login: String, password: String, accountType: String) {
        viewModelScope.launch {
            _updatingState.value = true
            loginRepository.authCheck(login, password, accountType)
                .flowOn(Dispatchers.IO)
                .catch {
                    _status.postValue(UiState.Error(it.message.toString()))
                    Log.e("LoginViewModel", "checkAuth: ${it.message}")
                }
                .collect { result ->
                    _updatingState.value = false
                    _status.postValue(result)
                }
        }
    }

    fun isTokenValid() {
        viewModelScope.launch {
            loginRepository.checkBearer()
                .flowOn(Dispatchers.IO)
                .catch { _status.postValue(UiState.Error(it.message.toString())) }
                .collect { result -> _status.postValue(result) }
        }
    }
}
