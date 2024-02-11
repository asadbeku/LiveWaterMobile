package uz.prestige.livewater.login.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val TAG = "LoginViewModel"
    private val repo = LoginRepository()

    private var _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
        get() = _status

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun checkAuth(login: String, password: String) {
        viewModelScope.launch {
            _updatingState.postValue(true)
            repo.authCheck(login, password)
                .catch {
                    Log.e(TAG, "checkAuth: $it")
                    _updatingState.postValue(false)
                    _errorMessage.postValue("Xatolik yuz berdi!")
                }
                .collect {
                    _updatingState.postValue(false)
                    _status.postValue(it)
                }
        }
    }


}