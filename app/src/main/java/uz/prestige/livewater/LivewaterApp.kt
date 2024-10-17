package uz.prestige.livewater

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp
import uz.prestige.livewater.utils.ConnectionBroadcastReceiver
import javax.inject.Inject

@HiltAndroidApp
class LivewaterApp : Application() {

    @Inject
    lateinit var receiver: ConnectionBroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()


        registerReceiver(receiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(receiver)
    }


}