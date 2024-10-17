package uz.prestige.livewater.dayver.constructor

interface FilterListener {
    fun onApply(startTime: String?, endTime: String?, deviceSerial: String?, regionId: String?)
}