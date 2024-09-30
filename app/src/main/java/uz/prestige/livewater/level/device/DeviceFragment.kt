package uz.prestige.livewater.level.device

import uz.prestige.livewater.level.regions.RegionsActivity
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
import uz.prestige.livewater.level.device.adapter.DeviceAdapter
import uz.prestige.livewater.level.device.add_device.AddNewDeviceActivity
import uz.prestige.livewater.level.device.view_model.DeviceViewModel
import uz.prestige.livewater.level.constructor.type.DeviceType
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.level.map.MapActivity
import uz.prestige.livewater.level.test.TestDeviceActivity


class DeviceFragment : Fragment(R.layout.devices_fragment) {

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

        addNewDevice()
    }

    private fun setupUI() {
        setStatusBarColor()
        initLastUpdateRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDevices()
            binding.swipeRefresh.isRefreshing = false
        }

        if (TokenManager.getRole(requireContext()) == "admin") {
            binding.toolbar.inflateMenu(R.menu.device_top_app_bar_admin)
        } else {
            binding.toolbar.inflateMenu(R.menu.device_top_app_bar_operator)
        }



        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuMap -> {
                    // Handle map item click
                    startActivity(Intent(requireContext(), MapActivity::class.java))
                    Snackbar.make(requireView(), "Menu map is clicked", Snackbar.LENGTH_SHORT)
                        .show()
                    true
                }

                R.id.menuRegion -> {

                    Snackbar.make(requireView(), "Regions menu is clicked", Snackbar.LENGTH_SHORT)
                        .show()

                    startActivity(Intent(requireContext(), RegionsActivity::class.java))
                    true
                }

                R.id.testDevice -> {
                    startActivity(Intent(requireContext(), TestDeviceActivity::class.java))

                    true
                }

                else -> false
            }
        }

        if (TokenManager.getRole(requireContext()) == "admin") {
            binding.addDeviceButton.visibility = View.VISIBLE
        } else {
            binding.addDeviceButton.visibility = View.GONE
        }


    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun addNewDevice() {
        binding.addDeviceButton.setOnClickListener {
            val intent = Intent(requireContext(), AddNewDeviceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeDevice(position: Int) {
        val id = viewModel.getDeviceId(position)
        val deviceInfo: DeviceType = viewModel.getDeviceDataById(id)!!

        val intent = Intent(requireContext(), AddNewDeviceActivity::class.java)

        // Create a bundle to pass data to the activity
        val bundle = Bundle()
        bundle.putParcelable("deviceInfo", deviceInfo) // Assuming uz.prestige.livewater.constructor.type.DeviceType is Parcelable

        // Put the bundle into the intent
        intent.putExtra("bundle", bundle)

        // Start the activity
        startActivity(intent)
    }

    private fun initLastUpdateRecyclerView() {
        viewModel.getDevices()

        deviceAdapter = DeviceAdapter { changeDevice(it) }

        with(binding.deviceRecycler) {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
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
        if (isUpdating) {
            binding.deviceRecycler.visibility = View.GONE
            binding.shimmerRecycler.visibility = View.VISIBLE
        } else {
            binding.deviceRecycler.visibility = View.VISIBLE
            binding.shimmerRecycler.visibility = View.GONE
        }
    }

}