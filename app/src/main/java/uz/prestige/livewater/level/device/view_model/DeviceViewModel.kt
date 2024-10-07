package uz.prestige.livewater.level.device.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.constructor.type.DeviceType
import uz.prestige.livewater.utils.LevelDevicePagingSource
import uz.prestige.livewater.utils.RouteConfigPagingSource

class DeviceViewModel : ViewModel() {

    private val repository = DeviceRepository()

    private val _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean> get() = _updatingState

    private val _deviceData = MutableLiveData<DeviceType>()
    val deviceData: LiveData<DeviceType> get() = _deviceData

    fun fetchDeviceData() = Pager(
        config = PagingConfig(pageSize = 6),
        pagingSourceFactory = { LevelDevicePagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun getDeviceDataById(id: String) {
        viewModelScope.launch {
            _updatingState.value = true // Simplified `postValue` to `value`
            try {
                repository.getDeviceDataById(id)
                    .flowOn(Dispatchers.IO)
                    .collect { deviceData ->
                        Log.d("DeviceViewModel", "Received device data: $deviceData")
                        _deviceData.value = deviceData
                    }
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error fetching device data: ${e.message}")
            } finally {
                _updatingState.value = false
            }
        }
    }

    fun saveDeviceId(deviceId: String) = repository.saveDeviceId(deviceId)

    fun getDeviceId(position: Int): String = repository.getDeviceId(position)
}
