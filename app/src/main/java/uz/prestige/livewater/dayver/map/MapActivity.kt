package uz.prestige.livewater.dayver.map

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityMapBinding
import uz.prestige.livewater.dayver.map.view_model.MapType
import uz.prestige.livewater.dayver.map.view_model.MapViewModel

class MapActivity : AppCompatActivity() {

    // Binding for view elements
    private var _binding: ActivityMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView: MapView

    private val locations = listOf(
        MapType(point = Point(40.644104, 72.247934), title = "456123789", isWorking = false),
        MapType(point = Point(40.644918, 72.249261), title = "7789456123", isWorking = true),
        MapType(point = Point(40.645068, 72.248031), title = "123456789", isWorking = true),
        MapType(point = POINT, title = "321654987", isWorking = true),
        MapType(point = POINT1, title = "654987321", isWorking = true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeMapKit()
        setupUI()
        initializeMap()
        observeViewModel()
    }

    private fun initializeMapKit() {
        MapKitFactory.setApiKey("c8fef523-5f77-4426-849c-4ef407cbb9c9")
        MapKitFactory.initialize(this)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initializeMap() {
        mapView = binding.mapview
        mapView.map.move(
            CameraPosition(POINT1, 17.0f, 150.0f, 30.0f),
            Animation(Animation.Type.SMOOTH, 3f),
            null
        )
    }

    private fun addPlaceMarkListener(point: Point, title: String, isWorking: Boolean) {
        val viewColor = getPlaceMarkView(isWorking)
        mapView.map.mapObjects.addPlacemark(point, viewColor).setText(title)
    }

    private fun getPlaceMarkView(isWorking: Boolean): ViewProvider {
        val pinDrawable = if (isWorking) R.drawable.map_pin else R.drawable.map_pin_red
        return ViewProvider(View(this).apply {
            background = getDrawable(pinDrawable)
        })
    }

    private fun observeViewModel() {
        viewModel.getMapCoordinates()
        viewModel.mapCoordinates.observe(this) { coordinates ->
            coordinates.forEach {
                addPlaceMarkListener(it.point, it.title, it.isWorking)
            }
        }
    }

    private fun showToast(message: String) {
        Snackbar.make(binding.mapMainContainer, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupUI() {
        window.apply {
            statusBarColor = ContextCompat.getColor(context, R.color.colorPrimary)
            decorView.systemUiVisibility = 0
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val POINT = Point(40.646011, 72.249908)
        private val POINT1 = Point(40.644515, 72.248888)
    }
}
