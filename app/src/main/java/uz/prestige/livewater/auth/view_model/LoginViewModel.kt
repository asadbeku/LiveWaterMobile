package uz.prestige.livewater.auth.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.device.UiState

class LoginViewModel : ViewModel() {

    private val TAG = "LoginViewModel"
    private val repo = LoginRepository()

    private var _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
        get() = _status

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    private val _error = MutableLiveData<UiState>()
    val error: LiveData<UiState> get() = _error

    fun checkAuth(context: Context, login: String, password: String, accountType: String) {
        viewModelScope.launch {

            _updatingState.postValue(true)

            repo.authCheck(context, login, password, accountType)
                .catch {
                    Log.e(TAG, "checkAuth: $it")
                    _updatingState.postValue(false)
                    _error.postValue(UiState.Error(it.message.toString()))
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    when (it) {
                        "Login" -> {
                            _updatingState.postValue(false)
                            _error.postValue(UiState.Success("Login"))
                        }

                        else -> {
                            _updatingState.postValue(false)
                            _error.postValue(UiState.Error("Xatolik yuz berdi!"))
                        }
                    }
                }
        }
    }
}