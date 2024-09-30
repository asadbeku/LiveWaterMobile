package uz.prestige.livewater.dayver.device.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.constructor.type.DeviceType
import kotlin.time.Duration.Companion.seconds

class DeviceViewModel : ViewModel() {

    private val repository = DeviceRepository()

    private val _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    private val _devicesList = MutableLiveData<List<DeviceType>>()
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
                    delay(1.seconds)
                    _updatingState.postValue(false)
                    _devicesList.postValue(it)
                }
        }
    }

    fun getDeviceDataById(id: String): DeviceType? {
        return _devicesList.value?.firstOrNull { it.id == id }
    }

    fun getDeviceId(position: Int): String {
        return _devicesList.value?.get(position)?.id ?: ""
    }


}