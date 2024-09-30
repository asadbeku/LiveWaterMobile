package uz.prestige.livewater.level.constructor.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.constructor.type.RegionType

class FilterViewModel : ViewModel() {

    private val repository = ConstructorRepository()
    private var _regionsLiveData = MutableLiveData<List<RegionType>>()
    val regionsLiveData get() = _regionsLiveData

    private var _serialNumbersLiveData = MutableLiveData<List<String>>()
    val serialNumbersLiveData get() = _serialNumbersLiveData

    fun getRegions() {
        viewModelScope.launch {
            repository.getRegions()
                .catch {
                    Log.e("TAG", "getRegions: ${it.message}")
                }
                .flowOn(Dispatchers.IO)
                .collect { regions ->
                    val list = regions.toMutableList()
                    list.add(0, RegionType("all", 1, "Barcha hududlar", 0))
                    _regionsLiveData.postValue(list)
                }
        }
    }

    fun getDevices(regionId: String) {
        viewModelScope.launch {

            repository.getDevicesSerialByRegion(regionId)
                .catch {
                    Log.e("TAG", "getDevices: ${it.message}")
                }
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    _serialNumbersLiveData.postValue(list.map { it.serialNumber })
                }
        }
    }

    fun getDevicesByRegionId(regionId: String) {
        viewModelScope.launch {

            repository.getDevicesByRegionId(regionId)
                .catch {
                    Log.e("TAG", "getDevices: ${it.message}")
                }
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    _serialNumbersLiveData.postValue(list.map { it.serialNumber })
                }
        }
    }

    fun getDevicesIdBySerialNumber(deviceSerial: String): String {
        return repository.getDeviceIdBySerialNumber(deviceSerial)
    }

    fun getRegionIdByRegionName(regionName: String) =
        _regionsLiveData.value?.find { it.name == regionName }?.id ?: "all"
}