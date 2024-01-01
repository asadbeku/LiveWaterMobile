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

    fun checkAuth(login: String, password: String) {
        viewModelScope.launch {

            repo.authCheck(login, password)
                .catch {
                    Log.e(TAG, "checkAuth: $it")
                }
                .collect {
                    _status.postValue(it)
                }
        }
    }


}