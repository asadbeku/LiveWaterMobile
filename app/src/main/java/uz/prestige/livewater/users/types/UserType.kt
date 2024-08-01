package uz.prestige.livewater.users.types

import android.os.Parcel
import android.os.Parcelable

data class UserType(
    val id: String,
    val numbering: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val phoneNumber: String,
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
        parcel.writeString(phoneNumber)
        parcel.writeString(role)
        parcel.writeString(regionId)
        parcel.writeInt(devicesCount)
        parcel.writeLong(updatedAt)
        parcel.writeLong(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserType> {
        override fun createFromParcel(parcel: Parcel): UserType {
            return UserType(parcel)
        }

        override fun newArray(size: Int): Array<UserType?> {
            return arrayOfNulls(size)
        }
    }
}
