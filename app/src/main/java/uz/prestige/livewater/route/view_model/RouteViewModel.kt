package uz.prestige.livewater.route.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.route.types.BaseDataType
import uz.prestige.livewater.route.types.RouteType

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

    fun getRouteList() {
        viewModelScope.launch {
            _updatingState.postValue(true)
            repository.getRouteListFlow()
                .catch {
                    Log.d("RouteViewModel", "$it")
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
                    Log.d("RouteViewModel", "$it")
                }
                .flowOn(Dispatchers.IO)
                .collect { basedata ->
                    _updatingStateById.postValue(false)
                    _baseDataById.postValue(basedata)
                }

        }
    }


}