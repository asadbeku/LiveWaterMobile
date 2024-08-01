package uz.prestige.livewater.users.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import uz.prestige.livewater.constructor.type.RegionType
import uz.prestige.livewater.device.UiState

class AddUserViewModel : ViewModel() {

    private val _finish = MutableLiveData<Boolean>()
    val finish get() = _finish

    private val repository = AddUserRepository()

    private var regionsList = listOf<RegionType>()

    val regionsFlow: Flow<List<RegionType>> = repository.regionsList

    private val _message = MutableLiveData<UiState>()
    val message: LiveData<UiState> get() = _message

    fun getRegions() {
        viewModelScope.launch {
            repository.getRegionsFromNetwork()
        }
    }

    fun addUser(userJson: JsonObject) {
        viewModelScope.launch {
            repository.addUser(userJson)
                .catch {
                    _message.postValue(UiState.Error("${it.message}"))
                }
                .collect {
                    if (it) {
                        _message.postValue(UiState.Success("Added"))
                        delay(1000)
                        _finish.postValue(true)
                    } else {
                        _message.postValue(UiState.None)
                    }

                }

            repository.regionsList.collectLatest {
                regionsList = it
            }
        }


    }

    fun changeUserInfo(id: String, userJson: JsonObject) {
        viewModelScope.launch {
            repository.changeUser(id, userJson)
                .catch {
                    _message.postValue(UiState.Error("${it.message}"))
                }
                .collect {
                    _message.postValue(UiState.Success("Changed"))
                    delay(1000)
                    _finish.postValue(true)
                }
        }
    }

    fun getRegionId(regionName: String, regionsList: List<RegionType>): String {
        return regionsList.firstOrNull { it.name == regionName }?.id ?: ""
    }

    fun removeUser(id: String) {
        viewModelScope.launch {
            repository.removeUser(id)
                .catch { _message.postValue(UiState.Error("Remove")) }
                .collect {
                    _message.postValue(UiState.Success("Removed"))
                    delay(1000)
                    _finish.postValue(true)
                }
        }

    }

    fun getRegionName(regionId: String, regionsList: List<RegionType>): String {
        return regionsList.firstOrNull() { it.id == regionId }?.name ?: ""
    }


}