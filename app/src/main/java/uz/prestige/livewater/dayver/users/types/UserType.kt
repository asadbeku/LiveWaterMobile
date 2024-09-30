package uz.prestige.livewater.dayver.users.types

import android.os.Parcel
import android.os.Parcelable

data class DayverUserType(
    val id: String,
    val numbering: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String,
    val regionId: String,
    val devicesCount: Int,
    val updatedAt: Long,
    val createdAt: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(numbering)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(username)
        parcel.writeString(role)
        parcel.writeString(regionId)
        parcel.writeInt(devicesCount)
        parcel.writeLong(updatedAt)
        parcel.writeLong(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DayverUserType> {
        override fun createFromParcel(parcel: Parcel): DayverUserType {
            return DayverUserType(parcel)
        }

        override fun newArray(size: Int): Array<DayverUserType?> {
            return arrayOfNulls(size)
        }
    }
}
