package uz.prestige.livewater.device.type

import android.net.Uri

data class DeviceDataPassType(
    val serialNumber: String,
    val privateKey: String,
    val location: String,
    val objectName: String,
    val regionId: String,
    val ownerId: String,
    val uri: Uri
)
