package uz.prestige.livewater.dayver.route.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.utils.UiState
import uz.prestige.livewater.dayver.route.types.BaseDataType
import uz.prestige.livewater.utils.DayverRouteSourceFactory
import javax.inject.Inject

@HiltViewModel
class DayverRouteViewModel @Inject constructor(
    private val repository: RouteRepository,
    private val dayverRouteSourceFactory: DayverRouteSourceFactory
) : ViewModel() {

    private val _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean> get() = _updatingState

    private val _updatingStateById = MutableLiveData<Boolean>()
    val updatingStateById: LiveData<Boolean> get() = _updatingStateById

    private val _baseDataById = MutableLiveData<BaseDataType>()
    val baseDataById: LiveData<BaseDataType> get() = _baseDataById

    private val _error = MutableLiveData<UiState<*>>()
    val error: LiveData<UiState<*>> get() = _error

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
        pagingSourceFactory = { dayverRouteSourceFactory }
    ).flow.cachedIn(viewModelScope)

    fun saveId(id: String) = repository.saveId(id)

    fun getIdByPosition(position: Int) = repository.getId(position)

}
