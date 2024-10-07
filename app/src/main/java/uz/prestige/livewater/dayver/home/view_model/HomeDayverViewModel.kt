package uz.prestige.livewater.dayver.home.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import uz.prestige.livewater.dayver.types.LastUpdateTypeDayver
import uz.prestige.livewater.level.home.types.DeviceStatuses
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.level.home.view_model.HomeRepository
import kotlin.time.Duration.Companion.seconds

class HomeDayverViewModel : ViewModel() {

    private val repository = HomeDayverRepository()

    private val _devicesStatuses = MutableLiveData<DeviceStatuses>()
    val deviceStatuses: LiveData<DeviceStatuses>
        get() = _devicesStatuses

    private val _lastUpdatesList = MutableLiveData<List<LastUpdateTypeDayver>>()
    val lastUpdatesList: LiveData<List<LastUpdateTypeDayver>>
        get() = _lastUpdatesList

    val updatingState = MutableLiveData<Boolean>()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getDevicesStatusesAndLastUpdates() {
        viewModelScope.launch {
            try {
                updatingState.postValue(true)

                // Fetch lastUpdates and wait until completed
                val lastUpdates = repository.getLastUpdates()
                    .catch { e -> handleError(e, "getLastUpdates") }
                    .flowOn(Dispatchers.IO)
                    .firstOrNull() ?: emptyList()

                // Update LiveData for lastUpdates
                _lastUpdatesList.postValue(lastUpdates)

                // Fetch deviceStatuses only after lastUpdates are done
                val deviceStatuses = repository.getDevicesStatusesFlow()
                    .catch { e -> handleError(e, "getDevicesStatusesFlow") }
                    .flowOn(Dispatchers.IO)
                    .firstOrNull() ?: DeviceStatuses("0", "0", "0")

                // Post deviceStatuses LiveData
                _devicesStatuses.postValue(deviceStatuses)

            } finally {
                updatingState.postValue(false)
            }
        }
    }



    private fun handleError(e: Throwable, functionName: String) {
        _error.postValue(e.message ?: "Unknown error")
        Log.e("HomeDayverViewModel", "$functionName: $e")
    }
}
