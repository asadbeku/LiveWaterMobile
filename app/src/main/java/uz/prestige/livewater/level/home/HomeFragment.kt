package uz.prestige.livewater.level.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import uz.prestige.livewater.ActivityAbout
import uz.prestige.livewater.R
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.level.home.types.DeviceStatuses
import uz.prestige.livewater.level.home.view_model.HomeViewModel
import uz.prestige.livewater.auth.LoginActivity
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.databinding.FragmentLevelHomeBinding
import uz.prestige.livewater.level.home.adapter.LastUpdatesAdapter

class HomeFragment : Fragment(R.layout.fragment_level_home) {

    private var _binding: FragmentLevelHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var lastUpdateAdapter: LastUpdatesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()  // Set up observers before data retrieval
        setupUI()
    }

    private fun setupUI() {
        setStatusBarColor()
        initToolbar()
        initLastUpdateRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            title = "Asosiy - Level"
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.exit -> handleExit()
                    R.id.about -> navigateToAbout()
                    else -> false
                }
            }
        }
    }

    private fun initLastUpdateRecyclerView() {
        lifecycleScope.launch {
            lastUpdateAdapter = LastUpdatesAdapter()
        }

        with(binding.lastUpdateRecycler) {
            adapter = lastUpdateAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun observeViewModel() {

        viewModel.deviceStatuses.observe(viewLifecycleOwner) { devicesStatuses ->
            updateDeviceStatusUI(devicesStatuses)
        }

        viewModel.lastUpdatesList.observe(viewLifecycleOwner) { updatingList ->
            updateLastUpdateRecyclerView(updatingList)
        }

        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            updateUpdatingTextVisibility(isUpdating)
            if (!isUpdating) {
                binding.swipeRefresh.isRefreshing = false  // Stop refreshing when done
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            showError(error)
        }

        // Fetch data after setting up observers
        refreshData()


    }

    private fun refreshData() {
        viewModel.getDevicesStatusesAndLastUpdates()
    }

    private fun handleExit(): Boolean {
        TokenManager.clearToken(requireContext())
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
        return true
    }

    private fun navigateToAbout(): Boolean {
        startActivity(Intent(requireContext(), ActivityAbout::class.java))
        return true
    }

    private fun showError(error: String) {
        Snackbar.make(requireView(), "Error: $error", Snackbar.LENGTH_LONG).show()
    }

    private fun updateDeviceStatusUI(devicesStatuses: DeviceStatuses) {
        with(binding) {
            allDevices.text = devicesStatuses.all
            activeDevices.text = devicesStatuses.active
            inActiveDevices.text = devicesStatuses.inActive
        }
    }

    private fun updateLastUpdateRecyclerView(updatingList: List<LastUpdateType>) {
        val listFirst10 = updatingList.subList(0, 10)
        lastUpdateAdapter?.items = listFirst10  // Use DiffUtil for efficient updates
        lastUpdateAdapter?.notifyDataSetChanged()
    }

    private fun updateUpdatingTextVisibility(isUpdating: Boolean) {
        with(binding) {
            shimmerHorizontalContainers.visibility = if (isUpdating) View.VISIBLE else View.GONE
            shimmerRecycler.visibility = if (isUpdating) View.VISIBLE else View.GONE
            lastUpdateRecycler.visibility = if (isUpdating) View.GONE else View.VISIBLE
            horizontalContainers.visibility = if (isUpdating) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
