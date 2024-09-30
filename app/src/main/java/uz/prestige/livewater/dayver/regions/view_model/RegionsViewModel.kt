package uz.prestige.livewater.dayver.regions.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.dayver.device.UiState

class RegionsViewModel : ViewModel() {

    private val repository = RegionsRepository()

    private var _regions = MutableLiveData<List<RegionType>>()
    private var _message = MutableLiveData<UiState>()

    val regionsList get() = _regions
    val message get() = _message

    fun getRegions(delayInMils: Long) {
        viewModelScope.launch {

            delay(delayInMils)

            repository.getRegions()
                .catch {
                    _message.postValue(UiState.Error(it.message.toString()))
                }
                .collect {
                    _regions.postValue(it)
                }
        }
    }

    fun removeRegion(id: String) {
        viewModelScope.launch {
            repository.deleteRegion(id)
                .catch {
                    _message.postValue(UiState.Error(it.message.toString()))
                }
                .collect {
                    _message.postValue(UiState.Success(it.toString()))
                }
        }
    }

    fun updateRegion(id: String, json: JsonObject) {
        viewModelScope.launch {
            repository.updateRegion(id, json)
                .catch {
                    _message.postValue(UiState.Error(it.message.toString()))
                }
                .collect {
                    _message.postValue(UiState.Success(it.toString()))
                }
        }
    }

    fun addRegion(json: JsonObject) {
        viewModelScope.launch {
            repository.addRegion(json)
                .catch {
                    _message.postValue(UiState.Error(it.message.toString()))
                }
                .collect {
                    _message.postValue(UiState.Success(it))
                }
        }
    }

    fun getRegionByPosition(position: Int): RegionType {
        return _regions.value?.get(position) ?: RegionType("", 0, "", 0)
    }

}