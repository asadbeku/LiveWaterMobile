package uz.prestige.livewater.constructor.view_model

import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.constructor.type.ConstructorType

class ConstructorViewModel : ViewModel() {

    private val repository = ConstructorRepository()

    private var _constructorList = MutableLiveData<List<ConstructorType>>()
    val constructorList: LiveData<List<ConstructorType>>
        get() = _constructorList

    private var _updatingState = MutableLiveData<Boolean>()
    val updatingState: LiveData<Boolean>
        get() = _updatingState

    fun getConstructor(startTime: String, endTime: String, deviceSerial: String, regionId: String) {
        viewModelScope.launch {

            _updatingState.postValue(true)

            repository.getConstructorList(
                offset = 0,
                limit = 20,
                startTime = startTime,
                endTime = endTime,
                regionId = regionId,
                deviceSerial = deviceSerial
            )
                .catch {
                    _updatingState.postValue(false)
                    Log.e("ConstructorViewModel", "$it")
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _constructorList.postValue(it)
                    _updatingState.postValue(false)
                }
        }

    }


}