package uz.prestige.livewater.dayver.route.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.device.UiState
import uz.prestige.livewater.level.route.types.BaseDataType
import uz.prestige.livewater.level.route.types.RouteType

class RouteViewModel : ViewModel() {

    private val repository = RouteRepository()

    private var _routeList = MutableLiveData<List<RouteType>>()
    val routeList: LiveData<List<RouteType>>
        get() = _routeList

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    private var _updatingStateById = MutableLiveData<Boolean>()
    val updatingStateById: LiveData<Boolean>
        get() = _updatingStateById

    private var _baseDataById = MutableLiveData<BaseDataType>()
    val baseDataById: LiveData<BaseDataType>
        get() = _baseDataById

    private val _error = MutableLiveData<UiState>()
    val error: LiveData<UiState> get() = _error

    fun getRouteList() {
        viewModelScope.launch {
            _updatingState.postValue(true)
            repository.getRouteListFlow()
                .catch {
                    _error.postValue(UiState.Error(it.message.toString()))
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _updatingState.postValue(false)
                    _routeList.postValue(it)
                }
        }
    }

    fun getBaseDataById(position: Int) {
        viewModelScope.launch {

            val id = _routeList.value?.toList().orEmpty()[position].baseDataId

            _updatingStateById.postValue(true)
            repository.getBaseDataById(id)
                .catch {
                    _error.postValue(UiState.Error(it.message.toString()))
                }
                .flowOn(Dispatchers.IO)
                .collect { basedata ->
                    _updatingStateById.postValue(false)
                    _baseDataById.postValue(basedata)
                }
        }
    }


}