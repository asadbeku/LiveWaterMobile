package uz.prestige.livewater.home.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uz.prestige.livewater.home.types.DeviceLastUpdate
import uz.prestige.livewater.home.types.DeviceStatuses
import java.lang.Exception

class HomeViewModel : ViewModel() {
    private val TAG = "HomeViewModel"
    private val repo = HomeRepository()

    private var _devicesStatuses = MutableLiveData<DeviceStatuses>()
    val deviceStatuses: LiveData<DeviceStatuses>
        get() = _devicesStatuses

    private var _lastUpdatesList = MutableLiveData<List<DeviceLastUpdate>>()
    val lastUpdatesList: LiveData<List<DeviceLastUpdate>>
        get() = _lastUpdatesList

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    fun getDevicesStatusesAndLastUpdates() {
        viewModelScope.launch {
            val start = System.currentTimeMillis()
            _updatingState.postValue(true)

            try {
                val devicesStatusesResult = async { repo.getDevicesStatusesFlow().first() }.await()
                val lastUpdatesResult = async { repo.getLastUpdates().first() }.await()

                _devicesStatuses.postValue(devicesStatusesResult)
                _lastUpdatesList.postValue(lastUpdatesResult)
            } catch (e: Exception) {
                Log.e(TAG, "Error in getDevicesStatusesAndLastUpdates: $e")
            } finally {
                Log.d(TAG, "time: ${System.currentTimeMillis() - start}")
                _updatingState.postValue(false)
            }
        }
    }
}
