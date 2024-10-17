package uz.prestige.livewater.dayver.map.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.prestige.livewater.dayver.device.view_model.DeviceRepository
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _mapCoordinatesLiveData = MutableLiveData<List<MapType>>()
    val mapCoordinates get() = _mapCoordinatesLiveData

    fun getMapCoordinates() {
        viewModelScope.launch {
            repository.getDevicesList()
                .map { devices ->
                    devices.map { device ->
                        MapType(
                            title = device.serialNumber,
                            point = Point(device.lat.toDouble(), device.long.toDouble()),
                            isWorking = device.isWorking
                        )
                    }
                }
                .catch { exception ->
                    Log.e("MapViewModel", "Error fetching coordinates: $exception")
                }
                .collect { coordinates ->
                    _mapCoordinatesLiveData.postValue(coordinates)
                }
        }
    }
}
