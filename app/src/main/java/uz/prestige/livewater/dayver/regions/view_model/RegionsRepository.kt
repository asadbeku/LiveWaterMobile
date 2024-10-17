package uz.prestige.livewater.dayver.regions.view_model

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.flow
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.utils.convertDayverRegionSecondaryToRegionType
import uz.prestige.livewater.utils.convertToRegionType
import javax.inject.Inject

class RegionsRepository @Inject constructor(
    private val apiService: ApiServiceDayver
) {

    suspend fun getRegions() = flow {
        val response = apiService.getDayverRegions()

        if (response.isSuccessful) {
            val regionsList = response.body()!!.convertDayverRegionSecondaryToRegionType()

            emit(regionsList)
        } else {
            Throwable("Ma'lumotlarni qabuil qilishdagi xatolik")
        }
    }

    suspend fun updateRegion(id: String, json: JsonObject) = flow {
        val response = apiService.updateRegion(id, json)

        if (response.isSuccessful) {
            emit(response.body()?.msg)
        } else {
            emit(response.body()?.msg)
        }
    }

    suspend fun deleteRegion(id: String) = flow {
        val response = apiService.removeRegion(id = id)

        if (response.isSuccessful) response.body()
            ?.let { emit(it.msg) } else emit(response.body()?.msg)
    }

    suspend fun addRegion(json: JsonObject) = flow {

        val response = apiService.addRegion(json)

        if (response.isSuccessful && response.code() != 400) emit("Hudud muvofaqiyatli qo'shildi") else emit(
            "Xatolik mavjud"
        )
    }

}