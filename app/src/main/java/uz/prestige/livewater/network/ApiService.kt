package uz.prestige.livewater.network

import com.google.gson.JsonObject
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import uz.prestige.livewater.constructor.type.secondary.DeviceSecondaryType
import uz.prestige.livewater.constructor.type.secondary.RegionSecondaryType
import uz.prestige.livewater.constructor.type.secondary.SecondaryConstructor
import uz.prestige.livewater.device.type.secondary_type.OwnerSecondaryType
import uz.prestige.livewater.home.types.test.LastUpdateSecondaryType
import uz.prestige.livewater.regions.types.AddRegionResponseType
import uz.prestige.livewater.regions.types.RemoveRegionResponseType
import uz.prestige.livewater.route.types.secondary.BaseDataByIdSecondaryType
import uz.prestige.livewater.route.types.secondary.RouteSecondaryType
import uz.prestige.livewater.users.types.secondary.RegionsSecondaryType
import uz.prestige.livewater.users.types.secondary.UserSecondaryType

interface ApiService {

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun authCheck(
        @Field("username") username: String, @Field("password") password: String
    ): Response<Any>

    @GET("basedata/last-updated")
    suspend fun getLastUpdate(): Response<LastUpdateSecondaryType>

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

    @Multipart
    @PATCH("devices/{id}")
    suspend fun changeDeviceInfo(
        @Path("id") id: String,
        @Part("serie") serie: RequestBody,
        @Part("name") name: RequestBody,
        @Part("device_privet_key") device_privet_key: RequestBody,
        @Part("region") region: RequestBody,
        @Part("owner") owner: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("long") long: RequestBody
    ): Response<Any>

    @Multipart
    @PATCH("devices/{id}")
    suspend fun changeDeviceInfoWithFile(
        @Path("id") id: String,
        @Part("serie") serie: RequestBody,
        @Part("name") name: RequestBody,
        @Part("device_privet_key") device_privet_key: RequestBody,
        @Part("region") region: RequestBody,
        @Part("owner") owner: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("long") long: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Any>

    @GET("serverdata")
    suspend fun getRouteData(
        @Query("page[limit]") limit: Int, @Query("page[offset]") offset: Int
    ): Response<RouteSecondaryType>

    @GET("basedata/{id}")
    suspend fun getRouteInfoById(
        @Path("id") id: String
    ): Response<BaseDataByIdSecondaryType>


    @GET("/auth")
    suspend fun isValidToken(): Response<Any>

    @GET("/users")
    suspend fun getUsers(): Response<UserSecondaryType>

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Any>

    @PATCH("/users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body json: JsonObject): Response<Any>

    @POST("/users")
    suspend fun addUser(
        @Body json: JsonObject
    ): Response<Any>

    @DELETE("regions/{id}")
    suspend fun removeRegion(
        @Path("id") id: String
    ): Response<RemoveRegionResponseType>


    @PATCH("regions/{id}")
    suspend fun updateRegion(
        @Path("id") id: String, @Body json: JsonObject
    ): Response<RemoveRegionResponseType>

    @POST("regions")
    suspend fun addRegion(
        @Body json: JsonObject
    ): Response<AddRegionResponseType>

    @GET("/{serialNumber}")
    suspend fun makeRequestToDevice(@Path("serialNumber") serialNumber: String): Response<Any>

}