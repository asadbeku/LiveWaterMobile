package uz.prestige.livewater.level.home.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.level.home.types.DeviceStatuses

class HomeViewModel : ViewModel() {
    private val repository = HomeRepository()

    private val _devicesStatuses = MutableLiveData<DeviceStatuses>()
    val deviceStatuses: LiveData<DeviceStatuses>
        get() = _devicesStatuses

    private val _lastUpdatesList = MutableLiveData<List<LastUpdateType>>()
    val lastUpdatesList: LiveData<List<LastUpdateType>>
        get() = _lastUpdatesList

    val updatingState = MutableLiveData<Boolean>()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getDevicesStatusesAndLastUpdates() {
        viewModelScope.launch {
            try {
                repository.crashTest()
                updatingState.postValue(true)

                val lastUpdatesResult = async {
                    repository.getLastUpdates()
                        .catch { e ->
                            handleError(e, "getLastUpdates")
                        }
                        .flowOn(Dispatchers.IO)
                        .firstOrNull()
                }

                val devicesStatusesResult = async {
                    repository.getDevicesStatusesFlow()
                        .catch { e ->
                            handleError(e, "getDevicesStatusesFlow")
                        }
                        .flowOn(Dispatchers.IO)
                        .firstOrNull()
                }

                _devicesStatuses.postValue(devicesStatusesResult.await() ?: DeviceStatuses("0", "0", "0"))
                _lastUpdatesList.postValue(lastUpdatesResult.await() ?: emptyList())
            } finally {
                updatingState.postValue(false)
            }
        }
    }

    private fun handleError(e: Throwable, functionName: String) {
        _error.postValue(e.toString())
        Log.e("HomeViewModel", "$functionName: $e")
    }
}

