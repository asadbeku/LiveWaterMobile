package uz.prestige.livewater

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import uz.prestige.livewater.databinding.ActivityMapBinding
import uz.prestige.livewater.databinding.FragmentAboutBinding
import uz.prestige.livewater.utils.convertMillisToDateTime
import uz.prestige.livewater.utils.convertToMillis
import uz.prestige.livewater.utils.getExpiredDateInMills
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ActivityAbout : AppCompatActivity() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val packageManager = this.packageManager
        val packageName = this.packageName

        binding.apply {
            binding.nameOfApp.text = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    packageName,
                    0
                )
            )

            binding.version.text = packageManager.getPackageInfo(packageName, 0).versionName
            binding.packageName.text = packageName

            val lastUpdateDate =
                packageManager.getPackageInfo(
                    packageName,
                    0
                ).lastUpdateTime.convertMillisToDateTime()

            binding.lastUpdate.text = lastUpdateDate

            Log.d(
                "lastUpdate",
                "onCreate: ${
                    packageManager.getPackageInfo(
                        packageName,
                        0
                    ).lastUpdateTime
                }"
            )
        }
    }
}