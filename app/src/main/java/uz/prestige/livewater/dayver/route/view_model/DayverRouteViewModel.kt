package uz.prestige.livewater.dayver.route.view_model

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
import uz.prestige.livewater.dayver.device.UiState
import uz.prestige.livewater.dayver.route.types.BaseDataType
import uz.prestige.livewater.dayver.route.types.RouteType
import uz.prestige.livewater.utils.DayverRouteSourceFactory

class DayverRouteViewModel : ViewModel() {

    private val repository = RouteRepository()

    private val _routeList = MutableLiveData<List<RouteType>>()
    val routeList: LiveData<List<RouteType>> get() = _routeList

    private val _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean> get() = _updatingState

    private val _updatingStateById = MutableLiveData<Boolean>()
    val updatingStateById: LiveData<Boolean> get() = _updatingStateById

    private val _baseDataById = MutableLiveData<BaseDataType>()
    val baseDataById: LiveData<BaseDataType> get() = _baseDataById

    private val _error = MutableLiveData<UiState>()
    val error: LiveData<UiState> get() = _error

    fun getBaseDataById(id: String) {
        viewModelScope.launch {

            _updatingStateById.postValue(true)
            repository.getBaseDataById(id)
                .catch { throwable ->
                    Log.e("DayverRouteViewModel", "Error fetching base data: ${throwable.message}")
                    _updatingStateById.postValue(false)
                    _error.postValue(UiState.Error(throwable.message.toString()))
                }
                .flowOn(Dispatchers.IO)
                .collect { baseData ->
                    _updatingStateById.postValue(false)
                    _baseDataById.postValue(baseData)
                }
        }
    }

    fun fetchRouteList() = Pager(
        config = PagingConfig(10),
        pagingSourceFactory = { DayverRouteSourceFactory() }
    ).flow.cachedIn(viewModelScope)

    fun saveId(id: String) = repository.saveId(id)

    fun getIdByPosition(position: Int) = repository.getId(position)

}
