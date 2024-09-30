package uz.prestige.livewater.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import uz.prestige.livewater.dayver.types.LastUpdateTypeDayver
import uz.prestige.livewater.dayver.types.secondary.LastUpdateDayverSecondaryType
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.dayver.users.types.secondary.DayverUserSecondaryType
import uz.prestige.livewater.level.constructor.type.ConstructorType
import uz.prestige.livewater.level.constructor.type.DeviceType
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.level.constructor.type.secondary.DeviceSecondaryType
import uz.prestige.livewater.level.constructor.type.secondary.RegionSecondaryType
import uz.prestige.livewater.level.constructor.type.secondary.SecondaryConstructor
import uz.prestige.livewater.level.device.type.OwnerType
import uz.prestige.livewater.level.device.type.secondary_type.OwnerSecondaryType
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.level.home.types.test.LastUpdateSecondaryType
import uz.prestige.livewater.level.route.types.BaseDataType
import uz.prestige.livewater.level.route.types.RouteType
import uz.prestige.livewater.level.route.types.secondary.BaseDataByIdSecondaryType
import uz.prestige.livewater.level.route.types.secondary.RouteSecondaryType
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.level.users.types.secondary.UserSecondaryType
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Long.toFormattedTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    // Adding 5 hours to UTC time zone
    val date = Date(this + 5 * 3600 * 1000)

    return dateFormat.format(date)
}

fun Long.toFormattedDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this + 5 * 3600 * 1000)
    return dateFormat.format(date)
}

fun DeviceSecondaryType.convertDeviceSecondaryToDeviceType(): List<DeviceType> {
    var counter = 1
    return this@convertDeviceSecondaryToDeviceType.data.map { data ->
        DeviceType(
            id = data._id,
            numbering = counter,
            serialNumber = data.serie,
            regionId = data.region._id,
            regionName = data.region.name,
            devicePrivateKey = data.device_privet_key,
            long = data.long.toString(),
            lat = data.lat.toString(),
            ownerName = data.owner?.first_name + " " + data.owner?.last_name,
            createdAt = data.created_at,
            objectName = data.name,
            isWorking = data.isWorking
        ).also { counter++ }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(
    bindingFactory: (Fragment) -> T
): ReadOnlyProperty<Fragment, T> {
    return object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        init {
            this@viewBinding
                .viewLifecycleOwnerLiveData
                .observe(this@viewBinding, { owner: LifecycleOwner? ->
                    owner?.lifecycle?.addObserver(this)
                })
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            return binding ?: bindingFactory(thisRef).also { it ->
                binding = it
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }
}

fun RegionSecondaryType.convertRegionSecondaryToRegionType(): List<RegionType> {

    var counter = 1

    return this@convertRegionSecondaryToRegionType.data.map { data ->
        RegionType(
            id = data._id,
            name = data.name,
            deviceCount = data.devicesCount,
            numbering = counter
        ).also { counter++ }
    }
}

fun SecondaryConstructor.convertSecondaryTypeToConstructorType(): List<ConstructorType> {
    var counter = 1
    val convertedList = this@convertSecondaryTypeToConstructorType.data.map { data ->
        ConstructorType(
            id = data._id,
            serie = data.device.serie,
            numbering = counter.toString(),
            level = data.level.toString(),
            preassur = data.pressure.toString(),
            volume = data.volume.toString(),
            signal = if (data.signal == "good") true else false,
            dateInMillisecond = data.date_in_ms.toString(),
            regionId = ""
        ).also { counter++ }
    }

    return convertedList
}

fun OwnerSecondaryType.convertOwnerSecondaryToOwnerType(): List<OwnerType> {
    return this@convertOwnerSecondaryToOwnerType.data.map { data ->
        OwnerType(
            id = data.id,
            firstName = data.first_name,
            lastName = data.last_name,
            region = data.region,
            role = data.role,
            username = data.username
        )
    }
}

fun uz.prestige.livewater.dayver.device.type.secondary_type.OwnerSecondaryType.convertDayverOwnerSecondaryToOwnerType(): List<uz.prestige.livewater.dayver.device.type.OwnerType> {
    return this@convertDayverOwnerSecondaryToOwnerType.data.map { data ->
        uz.prestige.livewater.dayver.device.type.OwnerType(
            id = data.id,
            firstName = data.first_name,
            lastName = data.last_name,
            region = data.region,
            role = data.role,
            username = data.username
        )
    }
}

fun Uri.createPartFromUri(context: Context): MultipartBody.Part {
    // Get the content resolver from the context
    val contentResolver = context.contentResolver
    // Get the MIME type of the file
    val mimeType = contentResolver.getType(this@createPartFromUri)
    // Get the file name from the URI
    val fileName = DocumentFile.fromSingleUri(context, this@createPartFromUri)?.name
    // Get the input stream from the URI
    val inputStream = contentResolver.openInputStream(this@createPartFromUri)
    // Create a byte array output stream
    val outputStream = ByteArrayOutputStream()
    // Copy the input stream to the output stream
    inputStream?.copyTo(outputStream)
    // Create a request body from the output stream and the MIME type
    val requestBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), outputStream.toByteArray())
    // Create a multipart body part from the request body and the file name
    return MultipartBody.Part.createFormData("file", fileName, requestBody)
}

fun RouteSecondaryType.convertToRouteType(): List<RouteType> {
    return this@convertToRouteType.data.map { routeData ->
        RouteType(
            id = routeData._id,
            date = routeData.send_data_in_ms,
            privateKey = routeData.device_privet_key,
            statusCode = routeData.status_code,
            message = routeData.message,
            baseDataId = routeData.basedata
        )
    }
}

fun BaseDataByIdSecondaryType.convertToBaseDataType(): BaseDataType {
    return this@convertToBaseDataType.let { data ->
        BaseDataType(
            id = data._id,
            date = data.date_in_ms,
            level = data.level,
            volume = data.volume
        )
    }
}

fun String.convertToRequestBody(): RequestBody {
    return this@convertToRequestBody.toRequestBody(MultipartBody.FORM)
}

fun LastUpdateSecondaryType.convertTestTypeToLastUpdates(): List<LastUpdateType> {
    return this.mapIndexed { index, data ->
        LastUpdateType(
            id = data.id,
            serial = data.device.serie,
            numbering = (index + 1).toString(),
            level = data.level.toString(),
            pressure = "961.8",
            volume = data.volume,
            signal = data.signal == "good",
            time = data.dateInMs.toString(),
            name = data.device.name
        )
    }
}

fun LastUpdateDayverSecondaryType.convertSecondaryToPrimary(): List<LastUpdateTypeDayver> {
    return this.mapIndexed { index, data ->
        LastUpdateTypeDayver(
            id = data.id,
            serial = data.device.serie,
            numbering = (index + 1).toString(),
            level = data.level.toString(),
            salinity = data.salinity.toString(),
            temperature = data.temperature.toString(),
            signal = data.signal == "good",
            time = data.dateInMs.toString(),
            name = data.device.name
        )
    }
}

fun String.getExpiredDateInMills(): Long {

    // Decode the JWT token to get the expiration time
    val jwtPayload = this@getExpiredDateInMills.split(".")[1] // Extracting the payload
    val decodedPayload = String(Base64.decode(jwtPayload, Base64.DEFAULT))
    val payloadJsonObject = JSONObject(decodedPayload)
    val expirationTimeSeconds = payloadJsonObject.optLong("exp")

    return expirationTimeSeconds * 1000
}

fun String.getRoleFromJwt(): String {
    val jwtPayload = this.split(".")[1] // Extracting the payload
    val decodedBytes = Base64.decode(jwtPayload, Base64.URL_SAFE or Base64.NO_WRAP)
    val decodedPayload = String(decodedBytes, Charsets.UTF_8)
    val payloadJsonObject = JSONObject(decodedPayload)
    return payloadJsonObject.getJSONObject("user").getString("role")
}

fun UserSecondaryType.convertToUserType(): List<UserType> {

    return this@convertToUserType.data.mapIndexed { index, data ->
        UserType(
            id = data._id,
            numbering = (index + 1),
            firstName = data.first_name,
            lastName = data.last_name,
            username = data.username,
            role = data.role,
            regionId = data.region,
            phoneNumber = data.mobil_phone ?: "",
            createdAt = data.created_at.convertToMillis(),
            updatedAt = data.updated_at.convertToMillis(),
            devicesCount = data.devices.size
        )
    }

}

fun DayverUserSecondaryType.convertToUserType(): List<DayverUserType> {

    return this@convertToUserType.data.mapIndexed { index, data ->
        DayverUserType(
            id = data._id,
            numbering = (index + 1),
            firstName = data.first_name,
            lastName = data.last_name,
            username = data.username,
            role = data.role,
            regionId = data.region,
            createdAt = data.created_at.convertToMillis(),
            updatedAt = data.updated_at.convertToMillis(),
            devicesCount = data.devices.size
        )
    }

}

fun String.convertToMillis(): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    return try {
        val date = dateFormat.parse(this@convertToMillis)
        date?.time!!
    } catch (e: Exception) {
        0
    }

}

fun RegionSecondaryType.convertToRegionType(): List<RegionType> {


    return this@convertToRegionType.data.mapIndexed { index, region ->
        RegionType(region.id, index + 1, region.name, region.devicesCount)
    }
}

fun String.removeFirstThreeDigits(): String {

    val number: Long = this@removeFirstThreeDigits.toLong()
    // Convert the number to a string
    val numberAsString = number.toString()

    // Check if the number has at least 3 digits
    return if (numberAsString.length >= 3) {
        numberAsString.substring(3)
    } else {
        // If the number has less than 3 digits, return the number itself
        number.toString()
    }
}