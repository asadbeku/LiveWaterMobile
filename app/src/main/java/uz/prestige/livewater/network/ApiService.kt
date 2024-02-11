package uz.prestige.livewater.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import uz.prestige.livewater.constructor.type.secondary.DeviceSecondaryType
import uz.prestige.livewater.constructor.type.secondary.RegionSecondaryType
import uz.prestige.livewater.constructor.type.secondary.SecondaryConstructor
import uz.prestige.livewater.device.type.secondary_type.OwnerSecondaryType
import uz.prestige.livewater.home.types.test.LastUpdateTestType

interface ApiService {

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun authCheck(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Any>

    @GET("basedata")
    suspend fun getLastUpdate(
        @Query("page[limit]") limit: Int
    ): Response<LastUpdateTestType>

    @GET("basedata")
    suspend fun getConstructor(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[device]") device: String,
        @Query("filter[region]") region: String
    ): Response<SecondaryConstructor>

    suspend fun getConstructorByDeviceSerial(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[device]") device: String
    ): Response<SecondaryConstructor>

    @GET("basedata")
    suspend fun getConstructorByRegion(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[region]") region: String
    ): Response<SecondaryConstructor>

    @GET("basedata")
    suspend fun getConstructorByNone(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String
    ): Response<SecondaryConstructor>

    @GET("regions")
    suspend fun getRegions(): Response<RegionSecondaryType>

    @GET("devices")
    suspend fun getDevices(
        @Query("page[limit]") limit: Int
    ): Response<DeviceSecondaryType>

    @GET("users")
    suspend fun getOwners(): Response<OwnerSecondaryType>

    @Multipart
    @POST("devices")
    suspend fun addNewDevice(
        @Part("serie") serie: RequestBody,
        @Part("name") name: RequestBody,
        @Part("device_privet_key") device_privet_key: RequestBody,
        @Part("region") region: RequestBody,
        @Part("owner") owner: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("long") long: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Any>


}