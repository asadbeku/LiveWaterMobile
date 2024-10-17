package uz.prestige.livewater.level.device

import uz.prestige.livewater.level.regions.RegionsActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.DevicesFragmentBinding
import uz.prestige.livewater.level.device.adapter.DeviceAdapter
import uz.prestige.livewater.level.device.add_device.AddNewDeviceActivity
import uz.prestige.livewater.level.device.view_model.DeviceViewModel
import uz.prestige.livewater.level.constructor.type.DeviceType
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.level.device.adapter.DevicePagingAdapter
import uz.prestige.livewater.level.map.MapActivity
import uz.prestige.livewater.level.test.TestDeviceActivity

@AndroidEntryPoint
class DeviceFragment : Fragment(R.layout.devices_fragment) {

    private var _binding: DevicesFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DeviceViewModel by viewModels()
    private lateinit var deviceAdapter: DevicePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DevicesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        getDevices()
        addNewDevice()
    }

    private fun setupUI() {
        setStatusBarColor()
        initLastUpdateRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            getDevices()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuMap -> {
                    startActivity(Intent(requireContext(), MapActivity::class.java))
//                    Snackbar.make(requireView(), "Map menu clicked", Snackbar.LENGTH_SHORT).show()
                    true
                }

                R.id.menuRegion -> {
                    startActivity(Intent(requireContext(), RegionsActivity::class.java))
//                    Snackbar.make(requireView(), "Regions menu clicked", Snackbar.LENGTH_SHORT).show()
                    true
                }

                R.id.testDevice -> {
                    startActivity(Intent(requireContext(), TestDeviceActivity::class.java))
                    true
                }

                else -> false
            }
        }

        binding.addDeviceButton.visibility =
            if (TokenManager.getRole(requireContext()) == "admin") View.VISIBLE else View.GONE
    }

    private fun setStatusBarColor() {
        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
            decorView.systemUiVisibility = 0
        }
    }

    private fun addNewDevice() {
        binding.addDeviceButton.setOnClickListener {
            startActivity(Intent(requireContext(), AddNewDeviceActivity::class.java))
        }
    }

    private fun changeDevice(deviceInfo: DeviceType) {
        val intent = Intent(requireContext(), AddNewDeviceActivity::class.java).apply {
            putExtra("bundle", Bundle().apply {
                putParcelable("deviceInfo", deviceInfo)
            })
        }
        startActivity(intent)
    }

    private fun initLastUpdateRecyclerView() {
        deviceAdapter = DevicePagingAdapter {
            val deviceId = viewModel.getDeviceId(it)
            Log.d("DeviceFragment", "Device ID: $deviceId, position: $it")
            viewModel.getDeviceDataById(deviceId)
        }

        binding.deviceRecycler.apply {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            updateView(isUpdating)
        }

        viewModel.deviceData.observe(viewLifecycleOwner) { deviceInfo ->
            Log.d("DeviceFragment", "Received device data: $deviceInfo")
            changeDevice(deviceInfo)
        }
    }

    private fun getDevices() {
        lifecycleScope.launch {
            viewModel.fetchDeviceData().flowOn(Dispatchers.IO).collectLatest { pagingData ->

                deviceAdapter.addLoadStateListener { loadState ->
                    when (loadState.source.refresh) {
                        is LoadState.Error -> {
                            val errorState = loadState.source.refresh as LoadState.Error
                            Log.d("DeviceFragment", "Error: ${errorState.error.message}")
                            showErrorState()
                        }

                        is LoadState.Loading -> {
                            Log.d("DeviceFragment", "Loading")
                            showLoadingState()
                        }

                        is LoadState.NotLoading -> {
                            Log.d("DeviceFragment", "Loaded")
                            showContentState()
                        }
                    }
                }
                deviceAdapter.submitData(pagingData.map { device ->
                    Log.d("DeviceFragment", "Device ID: ${device.id}")
                    viewModel.saveDeviceId(device.id)
                    device
                })
            }
        }
    }

    private fun showLoadingState() {
        binding.shimmerRecycler.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
        binding.emptyTextView.visibility = View.GONE
        binding.deviceRecycler.visibility = View.GONE
    }

    private fun showErrorState() {
        binding.shimmerRecycler.stopShimmer()
        binding.shimmerRecycler.visibility = View.GONE
        binding.emptyTextView.visibility = View.VISIBLE
        binding.deviceRecycler.visibility = View.GONE
    }

    private fun showContentState() {
        binding.shimmerRecycler.stopShimmer()
        binding.shimmerRecycler.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
        binding.deviceRecycler.visibility = View.VISIBLE
    }

    private fun updateView(isUpdating: Boolean) {
        binding.deviceRecycler.visibility = if (isUpdating) View.GONE else View.VISIBLE
        binding.shimmerRecycler.visibility = if (isUpdating) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
