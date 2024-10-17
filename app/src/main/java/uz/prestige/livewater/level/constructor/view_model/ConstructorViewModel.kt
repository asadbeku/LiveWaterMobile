package uz.prestige.livewater.level.constructor.view_model

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
import uz.prestige.livewater.level.constructor.type.ConstructorType
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.utils.ConstructorPagingSource
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    private val apiService: ApiService,
    private val repository: ConstructorRepository
) : ViewModel() {

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    fun fetchConstructorData(
        startTime: String?,
        endTime: String?,
        deviceSerial: String,
        regionId: String
    ) = Pager(
        config = PagingConfig(pageSize = 6),
        pagingSourceFactory = {
            ConstructorPagingSource(
                apiService,
                startTime ?: "946666800000",
                endTime ?: "1893438000000",
                regionId,
                deviceSerial
            )
        }).flow.cachedIn(viewModelScope)

    fun downloadExcel() {
        repository.downloadFileToInternalStorage()
    }

    fun saveFileConfig(startTime: String?, endTime: String?, regionId: String, deviceId: String) =
        repository.generateFilterUrl(
            startTime = startTime,
            endTime = endTime,
            regionId = regionId,
            deviceId = deviceId
        )
}