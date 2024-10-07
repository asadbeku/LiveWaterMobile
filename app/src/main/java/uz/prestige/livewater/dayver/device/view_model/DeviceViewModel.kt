package uz.prestige.livewater.dayver.device.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.utils.DayverDeviceSourceFactory

class DeviceViewModel : ViewModel() {

    private val repository = DeviceRepository()

    private var _deviceData =
        MutableLiveData<uz.prestige.livewater.dayver.constructor.type.DeviceType>()
    val deviceData: LiveData<uz.prestige.livewater.dayver.constructor.type.DeviceType> get() = _deviceData

    fun fetchDevices() = Pager(
        config = PagingConfig(6),
        pagingSourceFactory = { DayverDeviceSourceFactory() }

    ).flow.cachedIn(viewModelScope)

    fun saveId(id: String) = repository.saveIds(id)

    fun getDeviceIdByPosition(position: Int): String = repository.getDeviceId(position)

    fun getDeviceDataById(id: String) {
        viewModelScope.launch {
            repository.getDeviceById(id)
                .catch {

                }
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _deviceData.postValue(it)
                }
        }
    }


}