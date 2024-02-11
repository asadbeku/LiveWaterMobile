package uz.prestige.livewater.device.add_device.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import uz.prestige.livewater.constructor.type.RegionType
import uz.prestige.livewater.device.type.DeviceDataPassType
import uz.prestige.livewater.device.type.OwnerType
import uz.prestige.livewater.device.type.secondary_type.OwnerSecondaryType

class AddDeviceViewModel : ViewModel() {
    private val repository = AddDeviceRepository()

    private var _regions = MutableLiveData<List<RegionType>>()
    val regions get() = _regions

    private var _owners = MutableLiveData<List<OwnerType>>()
    val owners get() = _owners

    fun getRegions() {
        viewModelScope.launch {
            repository.getRegions()
                .catch {
                    Log.d("AddDeviceViewModel", "$it")
                }
                .collect {
                    _regions.postValue(it)
                }
        }
    }

    fun getOwners() {
        viewModelScope.launch {
            repository.getOwners()
                .catch {
                    Log.d("AddDeviceViewModel", "$it")
                }
                .collect {
                    _owners.postValue(it)
                }
        }
    }

    fun addNewDevice(context: Context, device: DeviceDataPassType) {
        viewModelScope.launch {
            repository.addNewDevice(context = context, device)
                .catch {
                    Log.e("addNewDevice", "$it")
                }
                .collect {

                }
        }
    }

    fun getRegionId(regionName: String): String? {
        return regions.value?.filter { it.name == regionName }?.map { it.id }?.firstOrNull()
    }

    fun getOwnerId(ownerName: String): String? {
        return _owners.value?.filter { "${it.firstName} ${it.lastName}" == ownerName }
            ?.map { it.id }?.firstOrNull()
    }


}