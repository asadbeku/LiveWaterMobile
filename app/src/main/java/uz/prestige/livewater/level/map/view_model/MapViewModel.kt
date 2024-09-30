package uz.prestige.livewater.level.map.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.device.view_model.DeviceRepository
import uz.prestige.livewater.utils.ConstructorPagingSource

class MapViewModel : ViewModel() {

    private val repository = DeviceRepository()

    private var _mapCoordinatesLivedata = MutableLiveData<List<MapType>>()
    val mapCoordinates get() = _mapCoordinatesLivedata

    fun getMapCoordinates() {
        viewModelScope.launch {
            repository.getDevicesList()
                .catch {
                    Log.e("mapException", "It: $it")
                }
                .map { device ->
                    device.map {
                        MapType(
                            it.serialNumber,
                            Point(it.lat.toDouble(), it.long.toDouble()),
                            it.isWorking
                        )
                    }
                }
                .collect {
                    _mapCoordinatesLivedata.postValue(it)
                }
        }

    }

}