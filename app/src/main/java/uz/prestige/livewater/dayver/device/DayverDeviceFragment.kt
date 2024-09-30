package uz.prestige.livewater.dayver.device

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.DevicesFragmentBinding
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.dayver.device.adapter.DeviceAdapter
import uz.prestige.livewater.dayver.device.add_device.AddNewDeviceActivity
import uz.prestige.livewater.dayver.device.view_model.DeviceViewModel
import uz.prestige.livewater.dayver.regions.DayverRegionsActivity
import uz.prestige.livewater.dayver.map.MapActivity
import uz.prestige.livewater.level.test.TestDeviceActivity
import uz.prestige.livewater.level.constructor.type.DeviceType

class DayverDeviceFragment : Fragment(R.layout.devices_fragment) {

    private var _binding: DevicesFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeviceViewModel by viewModels()
    private var deviceAdapter: DeviceAdapter? = null

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
        deviceAdapter = DeviceAdapter { position -> changeDevice(position) }
        binding.deviceRecycler.apply {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.getDevices()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDevices()
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

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleAddDeviceButtonVisibility() {
        binding.addDeviceButton.visibility = if (TokenManager.getRole(requireContext()) == "admin") {
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

    private fun changeDevice(position: Int) {
        val deviceInfo: DeviceType = viewModel.getDeviceDataById(viewModel.getDeviceId(position))!!
        val intent = Intent(requireContext(), AddNewDeviceActivity::class.java).apply {
            putExtra("bundle", Bundle().apply { putParcelable("deviceInfo", deviceInfo) })
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.devicesList.observe(viewLifecycleOwner) {
            deviceAdapter?.submitList(it)
        }
        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            updateView(isUpdating)
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