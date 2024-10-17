package uz.prestige.livewater.dayver.constructor.view_model

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
import uz.prestige.livewater.dayver.constructor.type.ConstructorType
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.ConstructorPagingSource
import uz.prestige.livewater.utils.DayverConstructorPagingSource
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    private val apiService: ApiServiceDayver,
    private val repository: ConstructorRepository
) : ViewModel() {

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    fun fetchConstructorData(
        startTime: String?,
        endTime: String?,
        deviceSerial: String?,
        regionId: String?
    ) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            DayverConstructorPagingSource(
                apiService,
                startTime,
                endTime,
                regionId,
                deviceSerial
            )
        }).flow.cachedIn(viewModelScope).catch { Log.e("ConstructorViewModel", "$it") }
        .flowOn(Dispatchers.IO)

    fun generateFilterUrl(
        startTime: String?,
        endTime: String?,
        regionId: String?,
        deviceId: String?
    ) = repository.generateFilterUrl(startTime, endTime, regionId, deviceId)

    fun downloadExcel() {
        repository.downloadFileToInternalStorage()
    }
}