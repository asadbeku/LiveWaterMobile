package uz.prestige.livewater.dayver.device.add_device

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityAddDeviceBinding
import uz.prestige.livewater.dayver.device.type.DeviceDataPassType
import uz.prestige.livewater.dayver.constructor.type.DeviceType
import uz.prestige.livewater.dayver.device.add_device.view_model.AddDeviceViewModel
import uz.prestige.livewater.utils.UiState.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddNewDeviceActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // This will store current location info
    private var currentLocation: Location? = null

    private lateinit var binding: ActivityAddDeviceBinding
    private var fileUri: Uri? = null
    private lateinit var getContent: ActivityResultLauncher<String>
    private val viewModel: AddDeviceViewModel by viewModels()
    private var deviceInfo: DeviceType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setupUI()
        setupObservers()
        getBundleData()

    }

    private fun getBundleData() {
        val bundle = intent.getBundleExtra("bundle")
        deviceInfo = bundle?.getParcelable("deviceInfo")

        deviceInfo?.let { device ->
            binding.serialNumberInput.editText?.setText(device.serialNumber)
            binding.privateKeyInput.editText?.setText(device.devicePrivateKey)
            binding.locationInput.editText?.setText(device.lat + ", " + device.long)
            binding.region.editText?.setText(device.regionName)
            binding.objectNameInput.editText?.setText(device.objectName)
            binding.owner.editText?.setText(device.ownerName)
            binding.passportNameInput.editText?.setText("file.xls")

        }
        setupButtons()
    }

    private fun setupUI() {
        setStatusBarColor()
        setupContentLauncher()
    }

    private fun setupContentLauncher() {
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                fileUri = it
                setFileName(it)
            }
        }
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        window.decorView.systemUiVisibility = 0
    }

    private fun setupButtons() {

        if (deviceInfo != null) {
            binding.addDeviceButton.text = "O'zgartirish"
        } else {
            binding.addDeviceButton.text = "Qo'shish"
        }

        binding.addDeviceButton.setOnClickListener {
            if (isCheckedFields() && deviceInfo == null) {
                addNewDevice()
            } else if (isCheckedFields() && deviceInfo != null) {
                changeDevice()
            }
        }

        binding.locationInput.setEndIconOnClickListener {
            getFusedLocation()
        }

        binding.testDeviceButton.setOnClickListener {
            showRandomStatus()
        }

        binding.choosePassportButton.setOnClickListener {
            getContent.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getFusedLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                permission.REQUEST_CODE
            )
        } else {

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // Use the location object
                        val latitude = location.latitude.toString()
                        val longitude = location.longitude.toString()

                        binding.locationInput.editText?.setText("$latitude, $longitude")
                        Log.d("Location", "$latitude, $longitude")
                        // Do something with the coordinates
                    }
                }
        }
    }

    private fun changeDevice() {

        viewModel.changeDeviceInfo(
            applicationContext, DeviceDataPassType(
                deviceId = deviceInfo?.id ?: "null",
                serialNumber = binding.serialNumber.text.toString(),
                privateKey = binding.privateKey.text.toString(),
                location = binding.location.text.toString(),
                objectName = binding.objectName.text.toString(),
                regionId = getRegionId(binding.region.editText?.text.toString()),
                ownerId = getOwnerId(binding.owner.editText?.text.toString()),
                uri = fileUri
            )
        )
    }

    private fun setupObservers() {
        viewModel.getOwners()
        viewModel.getRegions()

        viewModel.owners.observe(this) { owners ->
            val ownerNames = owners.map { "${it.firstName} ${it.lastName}" }
            ownersDropDown(ownerNames)
        }

        viewModel.regions.observe(this) { regions ->
            val regionNames = regions.map { it.name }
            regionsDropDown(regionNames)
        }

        viewModel.error.observe(this@AddNewDeviceActivity) { state ->
            when (state) {
                is Error -> {
                    Snackbar.make(
                        binding.addDeviceMainContainer,
                        state.message,
                        Snackbar.LENGTH_LONG
                    ).setBackgroundTint(getColor(R.color.redPrimary)).show()
                }

                is Success<*> -> {
                    Snackbar.make(
                        binding.addDeviceMainContainer,
                        state.data.toString(),
                        Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(getColor(R.color.greenPrimary)).show()
                }

                else -> {
                    Snackbar.make(
                        binding.addDeviceMainContainer,
                        "Nomalum xabar",
                        Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(getColor(R.color.darkGray)).show()
                }
            }


        }
    }

    private fun ownersDropDown(list: List<String>) {
        binding.ownersAutoComplete.setAdapter(createAutoCompleteAdapter(list))
    }

    private fun regionsDropDown(list: List<String>) {
        binding.regionAutoComplete.setAdapter(createAutoCompleteAdapter(list))
    }

    private fun createAutoCompleteAdapter(list: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(this, R.layout.custom_dropdown_item, list)
    }

    private fun addNewDevice() {
        viewModel.addNewDevice(
            applicationContext,
            DeviceDataPassType(
                deviceId = "",
                serialNumber = binding.serialNumber.text.toString(),
                privateKey = binding.privateKey.text.toString(),
                location = binding.location.text.toString(),
                objectName = binding.objectName.text.toString(),
                regionId = getRegionId(binding.region.editText?.text.toString()),
                ownerId = getOwnerId(binding.owner.editText?.text.toString()),
                uri = fileUri ?: Uri.EMPTY
            )
        )
    }

    private fun showRandomStatus() {
        val list = listOf(
            "Ulanish muvofaqiyatli amalga oshirildi",
            "Ulanishda xatolik yuz berdi"
        )

        val status = list.random()
        val color = if (status == list[0]) R.color.greenPrimary else R.color.redPrimary

        Snackbar.make(binding.addDeviceMainContainer, status, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(color))
            .show()
    }

    private fun setFileName(uri: Uri) {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val filenameColumnIndex = cursor.getColumnIndexOrThrow("_display_name")
            if (cursor.moveToFirst()) {
                val filename = cursor.getString(filenameColumnIndex)
                binding.passportNameInput.editText?.setText(filename)
            }
        } ?: run {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
            Log.e("Cursor", "Empty cursor")
        }
    }

    private fun isCheckedFields(): Boolean {
        val fields = listOf(
            binding.serialNumberInput to "Seriya raqami talab qilinadi",
            binding.privateKeyInput to "Shaxsiy kalit talab qilinadi",
            binding.locationInput to "Joylashuv talab qilinadi",
            binding.region to "Hudud talab qilinadi",
            binding.objectNameInput to "Obyekt nomi kiritilishi kerak",
            binding.owner to "Egasi talab qilinadi",
            binding.passportNameInput to "Qurilma passportini tanlang"
        )

        fields.forEach { (textInput, errorMessage) ->
            if (textInput.editText?.text.isNullOrBlank()) {
                textInput.error = errorMessage
                return false
            } else {
                textInput.error = null
            }
        }
        return true
    }

    private fun getRegionId(regionName: String): String {
        return viewModel.getRegionId(regionName) ?: ""
    }

    private fun getOwnerId(ownerName: String): String {
        return viewModel.getOwnerId(ownerName) ?: ""
    }

    object permission {
        val REQUEST_CODE = 100
    }
}
