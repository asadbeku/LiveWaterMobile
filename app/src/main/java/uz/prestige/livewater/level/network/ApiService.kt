package uz.prestige.livewater.level.network

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import uz.prestige.livewater.dayver.types.secondary.LastUpdateDayverSecondaryType
import uz.prestige.livewater.dayver.users.types.UserDayverSecondaryType
import uz.prestige.livewater.dayver.users.types.secondary.DayverUserSecondaryType
import uz.prestige.livewater.level.constructor.type.DeviceType
import uz.prestige.livewater.level.constructor.type.secondary.DeviceSecondaryType
import uz.prestige.livewater.level.constructor.type.secondary.RegionSecondaryType
import uz.prestige.livewater.level.constructor.type.secondary.SecondaryConstructor
import uz.prestige.livewater.level.device.type.DeviceResponseSecondaryType
import uz.prestige.livewater.level.device.type.secondary_type.OwnerSecondaryType
import uz.prestige.livewater.level.home.types.test.LastUpdateSecondaryType
import uz.prestige.livewater.level.regions.types.AddRegionResponseType
import uz.prestige.livewater.level.regions.types.RemoveRegionResponseType
import uz.prestige.livewater.level.route.types.secondary.BaseDataByIdSecondaryType
import uz.prestige.livewater.level.route.types.secondary.RouteSecondaryType
import uz.prestige.livewater.level.users.types.UserResponseSecondaryDataType
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.level.users.types.secondary.Data
import uz.prestige.livewater.level.users.types.secondary.UserSecondaryType
import uz.prestige.livewater.utils.DayverDeviceSecondaryType

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

    @GET("basedata")
    suspend fun getDayverConstructor(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[device]") device: String,
        @Query("filter[region]") region: String
    ): Response<uz.prestige.livewater.dayver.constructor.type.secondary.SecondaryConstructor>

    @GET("basedata")
    suspend fun getConstructorByDeviceSerial(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[device]") device: String
    ): Response<SecondaryConstructor>

    @GET("basedata")
    suspend fun getDayverConstructorByDeviceSerial(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[device]") device: String
    ): Response<uz.prestige.livewater.dayver.constructor.type.secondary.SecondaryConstructor>

    @GET("basedata")
    suspend fun getConstructorByRegion(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[region]") region: String
    ): Response<SecondaryConstructor>

    @GET("basedata")
    suspend fun getDayverConstructorByRegion(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String,
        @Query("filter[region]") region: String
    ): Response<uz.prestige.livewater.dayver.constructor.type.secondary.SecondaryConstructor>

    @GET("basedata")
    suspend fun getConstructorByNone(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String
    ): Response<SecondaryConstructor>

    @GET("basedata")
    suspend fun getDayverConstructorByNone(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int,
        @Query("filter[start]") start: String,
        @Query("filter[end]") end: String
    ): Response<uz.prestige.livewater.dayver.constructor.type.secondary.SecondaryConstructor>

    @GET("regions")
    suspend fun getRegions(): Response<RegionSecondaryType>

    @GET("regions")
    suspend fun getDayverRegions(): Response<uz.prestige.livewater.dayver.constructor.type.secondary.RegionSecondaryType>

    @GET("devices")
    suspend fun getDevices(
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Response<DeviceSecondaryType>

    @GET("devices")
    suspend fun getDevicesByRegion(
        @Query("page[limit]") limit: Int,
        @Query("filter[region]") region: String
    ): Response<DeviceSecondaryType>

    @GET("devices")
    suspend fun getDayverDevices(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int
    ): Response<uz.prestige.livewater.dayver.constructor.type.secondary.DeviceSecondaryType>

    @GET("users")
    suspend fun getOwners(): Response<OwnerSecondaryType>

    @GET("users")
    suspend fun getDayverOwners(): Response<uz.prestige.livewater.dayver.device.type.secondary_type.OwnerSecondaryType>


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

    @GET("serverdata")
    suspend fun getDayverRouteData(
        @Query("page[limit]") limit: Int, @Query("page[offset]") offset: Int
    ): Response<uz.prestige.livewater.dayver.route.types.secondary.RouteSecondaryType>

    @GET("basedata/{id}")
    suspend fun getRouteInfoById(
        @Path("id") id: String
    ): Response<BaseDataByIdSecondaryType>

    @GET("basedata/{id}")
    suspend fun getDayverRouteInfoById(
        @Path("id") id: String
    ): Response<uz.prestige.livewater.dayver.route.types.secondary.BaseDataByIdSecondaryType>


    @GET("/auth")
    suspend fun isValidToken(): Response<Any>

    @GET("/users")
    suspend fun getLevelUsers(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int
    ): Response<UserSecondaryType>

    @GET("/users")
    suspend fun getDayverUsers(
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int
    ): Response<DayverUserSecondaryType>

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

    @GET("basedata/last-updated")
    suspend fun getLastUpdateDayver(): Response<LastUpdateDayverSecondaryType>

    @GET("devices/{id}")
    suspend fun getDeviceById(@Path("id") id: String): Response<DeviceResponseSecondaryType>

    @GET("users/{id}")
    suspend fun getLevelUserById(@Path("id") id: String): Response<UserResponseSecondaryDataType>

    @GET("users/{id}")
    suspend fun getDayverUserById(@Path("id") userId: String): Response<UserDayverSecondaryType>

    @GET("devices/{id}")
    suspend fun getDayverDeviceById(@Path("id") id: String): Response<DayverDeviceSecondaryType>
}