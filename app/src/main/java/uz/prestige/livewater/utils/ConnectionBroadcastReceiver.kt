package uz.prestige.livewater.utils

import android.app.ActivityManager
import android.app.Application
import android.app.Application.CONNECTIVITY_SERVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.prestige.livewater.no_connection.ActivityNoConnection
import uz.prestige.livewater.no_connection.InternetConnectionListener
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionBroadcastReceiver @Inject constructor(
    private val internetConnectionListener: InternetConnectionListener
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context?.let { isInternetAvailable(it) } == true) {
            internetConnectionListener.onInternetAvailable()
        } else {
            internetConnectionListener.onInternetLost()

            if (!isActivityOpened(context!!)) {
                navigateToNoConnectionActivity(context)
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun isActivityOpened(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(1)
        val currentActivity = runningTasks[0].topActivity
        return currentActivity?.className == ActivityNoConnection::class.java.name
    }

    private fun navigateToNoConnectionActivity(context: Context) {
        val intent = Intent(context, ActivityNoConnection::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
