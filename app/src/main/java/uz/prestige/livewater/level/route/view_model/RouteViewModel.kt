package uz.prestige.livewater.level.route.view_model

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
import uz.prestige.livewater.level.device.UiState
import uz.prestige.livewater.level.route.types.BaseDataType
import uz.prestige.livewater.level.route.types.RouteType
import uz.prestige.livewater.utils.RouteConfigPagingSource

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

    fun fetchRouteData() = Pager(
        config = PagingConfig(pageSize = 6),
        pagingSourceFactory = {
            RouteConfigPagingSource()
        }).flow.cachedIn(viewModelScope)

    fun getBaseDataById(id: String) {
        viewModelScope.launch {

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

    fun getRouteIdByPosition(position: Int): String = repository.getRouteIdByPosition(position)
    fun saveRouteId(id: String) = repository.saveRouteId(id)


}