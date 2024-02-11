package uz.prestige.livewater.device.add_device

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityAddDeviceBinding
import uz.prestige.livewater.device.add_device.view_model.AddDeviceViewModel
import uz.prestige.livewater.device.type.DeviceDataPassType
import java.io.File

class AddNewDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDeviceBinding
    private var fileUri: Uri? = null
    private lateinit var getContent: ActivityResultLauncher<String>
    private val viewModel: AddDeviceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        setStatusBarColor()
        setupContentLauncher()
        setupButtons()
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
        binding.addDeviceButton.setOnClickListener {
            if (isCheckedFields()) {
                addNewDevice()
            }
        }

        binding.testDeviceButton.setOnClickListener {
            showRandomStatus()
        }

        binding.choosePassportButton.setOnClickListener {
            getContent.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        }
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
}
