package uz.prestige.livewater.home.view_model

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
import uz.prestige.livewater.home.types.LastUpdateType
import uz.prestige.livewater.home.types.DeviceStatuses

class HomeViewModel() : ViewModel() {
    private val TAG = "HomeViewModel"
    private val repository = HomeRepository()

    private var _devicesStatuses = MutableLiveData<DeviceStatuses>()
    val deviceStatuses: LiveData<DeviceStatuses>
        get() = _devicesStatuses

    private var _lastUpdatesList = MutableLiveData<List<LastUpdateType>>()
    val lastUpdatesList: LiveData<List<LastUpdateType>>
        get() = _lastUpdatesList

    var updatingState = MutableLiveData<Boolean>()

    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

//    val flow = Pager(
//        PagingConfig(pageSize = 10)
//    ) {
//        MyPagingSource()
//    }.flow
//        .cachedIn(viewModelScope)

    fun getDevicesStatusesAndLastUpdates() {



        viewModelScope.launch {
            repository.crashTest()
            updatingState.postValue(true)
            val lastUpdatesResult = async {
                repository.getLastUpdates()
                    .catch { e ->
                        _error.postValue(e.toString())
                        Log.e("checkHome","getLastUpdates: $e")
                        updatingState.postValue(false)
                    }
                    .flowOn(Dispatchers.IO)
                    .firstOrNull()
            }.await()

            val devicesStatusesResult = async {
                repository.getDevicesStatusesFlow()
                    .catch { e ->
                        _error.postValue(e.toString())
                        Log.e("checkHome","getDevicesStatusesFlow: $e")
                        updatingState.postValue(false)
                    }
                    .flowOn(Dispatchers.IO)
                    .firstOrNull()
            }.await()

            _devicesStatuses.postValue(devicesStatusesResult ?: DeviceStatuses("0", "0", "0"))
            _lastUpdatesList.postValue(lastUpdatesResult ?: emptyList())
            updatingState.postValue(false)
        }
    }
}
