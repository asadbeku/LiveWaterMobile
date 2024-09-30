package uz.prestige.livewater.dayver.device.add_device.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.level.device.UiState
import uz.prestige.livewater.level.device.add_device.view_model.AddDeviceRepository
import uz.prestige.livewater.level.device.type.DeviceDataPassType
import uz.prestige.livewater.level.device.type.OwnerType

class AddDeviceViewModel : ViewModel() {
    private val repository = uz.prestige.livewater.dayver.device.add_device.view_model.AddDeviceRepository()

    private val _regions = MutableLiveData<List<RegionType>>()
    val regions: LiveData<List<RegionType>> get() = _regions

    private val _owners = MutableLiveData<List<uz.prestige.livewater.dayver.device.type.OwnerType>>()
    val owners: LiveData<List<uz.prestige.livewater.dayver.device.type.OwnerType>> get() = _owners

    private val _error = MutableLiveData<uz.prestige.livewater.dayver.device.UiState>()
    val error: LiveData<uz.prestige.livewater.dayver.device.UiState> get() = _error

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

    fun addNewDevice(context: Context, device: uz.prestige.livewater.dayver.device.type.DeviceDataPassType) {
        viewModelScope.launch {
            repository.addNewDevice(context, device)
                .catch { handleError(it) }
                .collect {
                    _error.postValue(uz.prestige.livewater.dayver.device.UiState.Success("Muvofaqiyatli qo'shildi"))
                }
        }
    }

    fun getRegionId(regionName: String): String? {
        return regions.value?.find { it.name == regionName }?.id
    }

    fun getOwnerId(ownerName: String): String? {
        return owners.value?.find { "${it.firstName} ${it.lastName}" == ownerName }?.id
    }

    fun changeDeviceInfo(context: Context, device: uz.prestige.livewater.dayver.device.type.DeviceDataPassType) {
        viewModelScope.launch {
            repository.changeDeviceInfo(context, device)
                .catch { handleError(it) }
                .collect {
                    _error.value = if (it) uz.prestige.livewater.dayver.device.UiState.Success("Muvofaqiyatli o'zgartirildi") else uz.prestige.livewater.dayver.device.UiState.Error("Qurilma maʼlumotlarini oʻzgartirib boʻlmadi")
                }
        }
    }

    private fun handleError(error: Throwable) {
        _error.postValue(uz.prestige.livewater.dayver.device.UiState.Error(error.message ?: "Noma'lum xatolik yuz berdi"))
        Log.e("AddDeviceViewModel", error.toString())
    }
}
