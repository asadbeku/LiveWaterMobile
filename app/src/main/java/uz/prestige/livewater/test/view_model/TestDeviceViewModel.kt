package uz.prestige.livewater.test.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.device.UiState
import uz.prestige.livewater.home.types.LastUpdateType

class TestDeviceViewModel : ViewModel() {
    private val repository = TestDeviceRepository()

    private var _message = MutableLiveData<UiState>()
    val message get() = _message

    var updatingState = MutableLiveData<Boolean>()

    private var _deviceInfo = MutableLiveData<LastUpdateType>()
    val deviceInfo get() = _deviceInfo

    fun checkSerialNumber(serialNumber: String) {
        viewModelScope.launch {
            repository.checkBySerialNumber(serialNumber)
                .catch {
                    _message.postValue(UiState.Error(it.message.toString()))

                }
                .flowOn(Dispatchers.IO)
                .collect { info ->
                    info?.let { _deviceInfo.postValue(it) }
                    _message.postValue(UiState.Success("Tekshiruv muvofaqiyatli yakunlandi"))
                    updatingState.postValue(false)
                }
        }

    }

    fun makeRequestToDevice(serialNumber: String) {
        viewModelScope.launch {
            repository.makeRequestToServer(serialNumber)
                .catch {
                    Log.d("checkDevice", it.toString())
                    _message.postValue(UiState.Error(it.message.toString()))
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    if (it) _message.postValue(UiState.Success("DeviceTest")) else _message.postValue(
                        UiState.Error("NotRegistered")
                    )
                }
        }
    }


}