package uz.prestige.livewater.dayver.constructor.type

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class DeviceType(
    val id: String,
    val numbering: Int,
    val serialNumber: String,
    val objectName: String,
    val devicePrivateKey: String,
    val long: String,
    val lat: String,
    val regionId: String,
    val regionName: String,
    val ownerName: String,
    val createdAt: String,
    val isWorking: Boolean
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt() != 0
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(numbering)
        parcel.writeString(serialNumber)
        parcel.writeString(objectName)
        parcel.writeString(devicePrivateKey)
        parcel.writeString(long)
        parcel.writeString(lat)
        parcel.writeString(regionId)
        parcel.writeString(regionName)
        parcel.writeString(ownerName)
        parcel.writeString(createdAt)
        parcel.writeInt(if (isWorking) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeviceType> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): DeviceType {
            return DeviceType(parcel)
        }

        override fun newArray(size: Int): Array<DeviceType?> {
            return arrayOfNulls(size)
        }
    }
}
