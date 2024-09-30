package uz.prestige.livewater.dayver.regions.view_model

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.utils.convertToRegionType

class RegionsRepository {

    suspend fun getRegions() = flow {
        val response = NetworkDayver.buildService(ApiService::class.java).getRegions()

        if (response.isSuccessful) {
            val regionsList = response.body()!!.convertToRegionType()

            emit(regionsList)
        } else {
            Throwable("Ma'lumotlarni qabuil qilishdagi xatolik")
        }
    }

    suspend fun updateRegion(id: String, json: JsonObject) = flow {
        val response = NetworkDayver.buildService(ApiService::class.java).updateRegion(id, json)

        if (response.isSuccessful) {
            emit(response.body()?.msg)
        } else {
            emit(response.body()?.msg)
        }
    }

    suspend fun deleteRegion(id: String) = flow {
        val response = NetworkDayver.buildService(ApiService::class.java).removeRegion(id = id)

        if (response.isSuccessful) response.body()
            ?.let { emit(it.msg) } else emit(response.body()?.msg)
    }

    suspend fun addRegion(json: JsonObject) = flow {

        val response = NetworkDayver.buildService(ApiService::class.java).addRegion(json)

        if (response.isSuccessful && response.code() != 400) emit("Hudud muvofaqiyatli qo'shildi") else emit(
            "Xatolik mavjud"
        )
    }

}