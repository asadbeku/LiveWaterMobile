package uz.prestige.livewater.device.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import uz.prestige.livewater.constructor.type.DeviceType

class DeviceViewModel : ViewModel() {

    private val repository = DeviceRepository()

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    private var _devicesList = MutableLiveData<List<DeviceType>>()
    val devicesList: LiveData<List<DeviceType>>
        get() = _devicesList

    fun getDevices() {
        viewModelScope.launch {

            _updatingState.postValue(true)

            repository.getDevicesList()
                .catch {
                    Log.e("DeviceViewModel", "$it")
                }
                .collect {
                    _updatingState.postValue(false)
                    _devicesList.postValue(it)
                }
        }
    }

    fun getDeviceId(position: Int): String {
        return _devicesList.value?.get(position)?.id ?: ""
    }


}