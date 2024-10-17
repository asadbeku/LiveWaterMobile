package uz.prestige.livewater.no_connection

interface InternetConnectionListener {
    fun onInternetAvailable()
    fun onInternetLost()

}