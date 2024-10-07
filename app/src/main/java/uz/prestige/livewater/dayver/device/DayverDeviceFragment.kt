package uz.prestige.livewater.dayver.device

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.DevicesFragmentBinding
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.dayver.device.adapter.DevicePagingAdapter
import uz.prestige.livewater.dayver.device.add_device.AddNewDeviceActivity
import uz.prestige.livewater.dayver.device.view_model.DeviceViewModel
import uz.prestige.livewater.dayver.regions.DayverRegionsActivity
import uz.prestige.livewater.dayver.map.MapActivity
import uz.prestige.livewater.level.test.TestDeviceActivity

class DayverDeviceFragment : Fragment(R.layout.devices_fragment) {

    private var _binding: DevicesFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeviceViewModel by viewModels()
    private var deviceAdapter: DevicePagingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DevicesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        setupAddDeviceButton()
        getDevice()
    }

    private fun setupUI() {
        setStatusBarColor()
        initRecyclerView()
        setupToolbar()
        setupSwipeRefresh()
        handleAddDeviceButtonVisibility()
    }

    private fun setStatusBarColor() {
        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
            decorView.systemUiVisibility = 0
        }
    }

    private fun initRecyclerView() {
        deviceAdapter = DevicePagingAdapter { position ->
            val id = viewModel.getDeviceIdByPosition(position)
            viewModel.getDeviceDataById(id)
        }
        binding.deviceRecycler.apply {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            getDevice()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupToolbar() {
        val menuRes = if (TokenManager.getRole(requireContext()) == "admin") {
            R.menu.device_top_app_bar_admin
        } else {
            R.menu.device_top_app_bar_operator
        }
        binding.toolbar.apply {
            inflateMenu(menuRes)
            setOnMenuItemClickListener { handleMenuClick(it.itemId) }
        }
    }

    private fun handleMenuClick(itemId: Int): Boolean {
        return when (itemId) {
            R.id.menuMap -> {
                startActivity(Intent(requireContext(), MapActivity::class.java))
                showSnackbar("Menu map is clicked")
                true
            }

            R.id.menuRegion -> {
                startActivity(Intent(requireContext(), DayverRegionsActivity::class.java))
                showSnackbar("Regions menu is clicked")
                true
            }

            R.id.testDevice -> {
                startActivity(Intent(requireContext(), TestDeviceActivity::class.java))
                true
            }

            else -> false
        }
    }

    private fun getDevice() {
        lifecycleScope.launch {
            viewModel.fetchDevices()
                .catch {
                    showSnackbar(it.message.toString())
                }
                .flowOn(Dispatchers.IO)
                .collectLatest { pagingData ->
                    deviceAdapter?.addLoadStateListener { loadState ->
                        when (loadState.source.refresh) {
                            is LoadState.Loading -> {
                                showLoadingState()
                            }

                            is LoadState.Error -> {
                                val errorMessage = loadState.source.refresh as LoadState.Error
                                showErrorState(errorMessage.error.message.toString())
                            }

                            is LoadState.NotLoading -> {
                                showContentState()
                            }
                        }

                    }
                    deviceAdapter?.submitData(pagingData.map {
                        viewModel.saveId(it.id)
                        it
                    })
                }
        }
    }

    private fun showLoadingState() {
        with(binding) {
            shimmerRecycler.visibility = View.VISIBLE
            shimmerRecycler.startShimmer()
            emptyTextView.visibility = View.GONE
            deviceRecycler.visibility = View.GONE
        }
    }

    private fun showErrorState(message: String) {
        with(binding) {
            emptyTextView.visibility = View.VISIBLE
            deviceRecycler.visibility = View.GONE
            shimmerRecycler.visibility = View.GONE
            shimmerRecycler.stopShimmer()

            Log.e("DayverDeviceFragment", message)
        }
    }

    private fun showContentState() {
        with(binding) {
            emptyTextView.visibility = View.GONE
            deviceRecycler.visibility = View.VISIBLE
            shimmerRecycler.visibility = View.GONE
            shimmerRecycler.stopShimmer()
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleAddDeviceButtonVisibility() {
        binding.addDeviceButton.visibility =
            if (TokenManager.getRole(requireContext()) == "admin") {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    private fun setupAddDeviceButton() {
        binding.addDeviceButton.setOnClickListener {
            startActivity(Intent(requireContext(), AddNewDeviceActivity::class.java))
        }
    }

    private fun changeDevice(deviceInfo: uz.prestige.livewater.dayver.constructor.type.DeviceType) {

        val intent = Intent(requireContext(), AddNewDeviceActivity::class.java).apply {
            putExtra("bundle", Bundle().apply { putParcelable("deviceInfo", deviceInfo) })
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.deviceData.observe(viewLifecycleOwner) {
            changeDevice(it)
        }
    }

    private fun updateView(isUpdating: Boolean) {
        binding.shimmerRecycler.visibility = if (isUpdating) View.VISIBLE else View.GONE
        binding.deviceRecycler.visibility = if (isUpdating) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}