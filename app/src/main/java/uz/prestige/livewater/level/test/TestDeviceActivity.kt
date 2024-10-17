package uz.prestige.livewater.level.test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityTestDeviceBinding
import uz.prestige.livewater.level.test.view_model.TestDeviceViewModel
import uz.prestige.livewater.utils.UiState
import uz.prestige.livewater.utils.toFormattedTime

@AndroidEntryPoint
class TestDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestDeviceBinding
    private val viewModel: TestDeviceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        observeSerialNumberInput()
        observers()
    }

    private fun setupUI() {
        window.apply {
            statusBarColor = ContextCompat.getColor(context, R.color.colorPrimary)
            decorView.systemUiVisibility = 0
        }

        binding.testDeviceBySerial.setOnClickListener {
            val serialNumber = binding.serialNumberInput.editText?.text.toString()
            if (serialNumber.isNotBlank()) {
                viewModel.makeRequestToDevice(serialNumber)
            } else {
                showMessageForUser("Qurilma seria raqamini kiriting", R.color.black)
            }
        }
    }

    private fun observeSerialNumberInput() {
        binding.serialNumberInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    showMessageForUser("Qurilma seria raqamini kiriting", R.color.black)
                }
            }
        })
    }

    private fun showMessageForUser(message: String, textColor: Int) {
        binding.messageForUser.apply {
            visibility = View.VISIBLE
            text = message
            setTextColor(ContextCompat.getColor(this@TestDeviceActivity, textColor))
        }
        binding.deviceInfo.visibility = View.GONE
    }

    private fun observers() {
        viewModel.message.observe(this) { state ->
            val message = when (state) {
                is UiState.Error -> state.message
                is UiState.Success -> state.data.toString()
                else -> getString(R.string.default_message)
            }

            when (message) {
                "DeviceTest" -> viewModel.updatingState.postValue(true)
                "NotRegistered" -> showMessageForUser(
                    "Bunday qurilma hali backend to'monidan ro'yxatga olinmagan!",
                    R.color.redPrimary
                )
                else -> {
                    Snackbar.make(binding.testDeviceMainContainer, message, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(
                            ContextCompat.getColor(
                                this,
                                if (state is UiState.Error) R.color.redPrimary else R.color.greenPrimary
                            )
                        ).show()
                }
            }
        }

        viewModel.deviceInfo.observe(this) { device ->
            with(binding) {
                serialNumberInput.editText?.setText(device.serial.toString())
                deviceObjectName.text = device.serial
                level.text = device.level
                volume.text = device.volume
                pressure.text = device.pressure
                time.text = device.time?.toLong()?.toFormattedTime()
                setupSignalTypeIcon(device.signal)
                messageForUser.visibility = View.GONE
                deviceInfo.visibility = View.VISIBLE
            }
        }

        viewModel.updatingState.observe(this) { isUpdating ->
            if (isUpdating) {
                lifecycleScope.launch {
                    for (i in 10 downTo 1) {
                        delay(1000)
                        binding.messageForUser.text =
                            "Qurilmaga tekshiruv protokoli jo'natildi. Tekshiruv tugashiga $i sekund qoldi"
                        binding.messageForUser.setTextColor(getColor(R.color.black))
                    }
                    viewModel.checkSerialNumber(binding.serialNumberInput.editText?.text.toString())
                }
            }
        }
    }

    private fun setupSignalTypeIcon(signal: Boolean) {
        val signalText = if (signal) "Yaxshi" else "Signal yo'q"
        val signalIcon = if (signal) R.drawable.icon_circle else R.drawable.icon_circle
        with(binding) {
            signalTypeText.text = signalText
            signalTypeIcon.setImageResource(signalIcon)
            val tintColor = ContextCompat.getColor(this@TestDeviceActivity, if (signal) R.color.greenPrimary else R.color.redPrimary)
            signalTypeIcon.setColorFilter(tintColor)
        }
    }
}
