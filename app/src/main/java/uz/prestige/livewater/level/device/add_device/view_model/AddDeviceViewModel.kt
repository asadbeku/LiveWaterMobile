package uz.prestige.livewater.level.device.add_device.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.device.type.DeviceDataPassType
import uz.prestige.livewater.level.device.type.OwnerType
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.utils.UiState
import javax.inject.Inject

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val repository: AddDeviceRepository
) : ViewModel() {

    private val _regions = MutableLiveData<List<RegionType>>()
    val regions: LiveData<List<RegionType>> get() = _regions

    private val _owners = MutableLiveData<List<OwnerType>>()
    val owners: LiveData<List<OwnerType>> get() = _owners

    private val _error = MutableLiveData<UiState<*>>()
    val error: LiveData<UiState<*>> get() = _error

    fun getRegions() {
        viewModelScope.launch {
            repository.getRegions()
                .catch { handleError(it) }
                .collect { _regions.postValue(it) }
        }
    }

    fun getOwners() {
        viewModelScope.launch {
            repository.getOwners()
                .catch { handleError(it) }
                .collect { _owners.postValue(it) }
        }
    }

    fun addNewDevice(context: Context, device: DeviceDataPassType) {
        viewModelScope.launch {
            repository.addNewDevice(context, device)
                .catch { handleError(it) }
                .collect {
                    _error.postValue(UiState.Success("Muvofaqiyatli qo'shildi"))
                }
        }
    }

    fun getRegionId(regionName: String): String? {
        return regions.value?.find { it.name == regionName }?.id
    }

    fun getOwnerId(ownerName: String): String? {
        return owners.value?.find { "${it.firstName} ${it.lastName}" == ownerName }?.id
    }

    fun changeDeviceInfo(context: Context, device: DeviceDataPassType) {
        viewModelScope.launch {
            repository.changeDeviceInfo(context, device)
                .catch { handleError(it) }
                .collect {
                    _error.value =
                        if (it) UiState.Success("Muvofaqiyatli o'zgartirildi") else UiState.Error("Qurilma maʼlumotlarini oʻzgartirib boʻlmadi")
                }
        }
    }

    private fun handleError(error: Throwable) {
        _error.postValue(UiState.Error(error.message ?: "Noma'lum xatolik yuz berdi"))
        Log.e("AddDeviceViewModel", error.toString())
    }
}
