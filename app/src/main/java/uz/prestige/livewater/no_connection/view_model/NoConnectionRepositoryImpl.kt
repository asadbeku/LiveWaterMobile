package uz.prestige.livewater.no_connection.view_model

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.prestige.livewater.no_connection.InternetConnectionListener
import javax.inject.Inject

class NoConnectionRepositoryImpl @Inject constructor(
    private val applicationScope: CoroutineScope
) : InternetConnectionListener {

    private val _connectionState = MutableStateFlow(true)
    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    override fun onInternetAvailable() {
        applicationScope.launch {
            Log.d("checkConnection", "Repository: onInternetAvailable")
            _connectionState.value = true
        }
    }

    override fun onInternetLost() {
        applicationScope.launch {
            Log.d("checkConnection", "Repository: onInternetLost")
            _connectionState.value = false
        }
    }
}