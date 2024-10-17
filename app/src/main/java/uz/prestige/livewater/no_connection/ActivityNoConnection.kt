package uz.prestige.livewater.no_connection

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.prestige.livewater.databinding.ActivityNoConnectionBinding
import uz.prestige.livewater.no_connection.view_model.NoConnectionViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class ActivityNoConnection : AppCompatActivity() {

    private lateinit var binding: ActivityNoConnectionBinding
    private val viewModel: NoConnectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.connectionState.collectLatest { isAvailable ->
                Log.d("checkConnection", "Activity: $isAvailable")
                if (isAvailable) {
                    finish()
                }
            }
        }
    }
}
