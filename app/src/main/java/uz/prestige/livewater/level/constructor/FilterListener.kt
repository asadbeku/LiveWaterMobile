package uz.prestige.livewater.level.constructor

interface FilterListener {
    fun onApply(startTime: String, endTime: String, deviceSerial: String, regionId: String)

    //        https://back2.livewater.uz/basedata?
    //        page[offset]=0&
    //        filter[start]=1704092400000&
    //        filter[end]=1706511600000&
    //        filter[device]=65acc099a09a9591e9d193bd&
    //        filter[region]=65abe7602a717cda8dc5a91b
}