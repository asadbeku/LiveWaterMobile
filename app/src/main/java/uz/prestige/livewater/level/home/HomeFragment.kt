package uz.prestige.livewater.level.home

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
import uz.prestige.livewater.level.home.types.LastUpdateType
import uz.prestige.livewater.level.home.types.DeviceStatuses
import uz.prestige.livewater.level.home.view_model.HomeViewModel
import uz.prestige.livewater.auth.LoginActivity
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.databinding.FragmentLevelHomeBinding
import uz.prestige.livewater.level.home.adapter.LastUpdatesAdapter

class HomeFragment : Fragment(R.layout.fragment_level_home) {

    private val TAG = "HomeFragment"

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

        setupUI()
        observeViewModel()

        binding.toolbar.menu

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            // Handle menu item clicks
            when (menuItem.itemId) {
                R.id.exit -> {
                    TokenManager.clearToken(requireContext())

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    true
                }
                // Add more cases for other menu items if needed
                else -> false
            }
        }
    }

    private fun setupUI() {
        setStatusBarColor()
        initLastUpdateRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDevicesStatusesAndLastUpdates()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initLastUpdateRecyclerView() {
        lastUpdateAdapter = LastUpdatesAdapter()

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

    private fun showError(error: String) {
        Snackbar.make(requireView(), "Error: $error", Snackbar.LENGTH_LONG).show()
    }

    private fun observeViewModel() {
        viewModel.getDevicesStatusesAndLastUpdates()

        viewModel.deviceStatuses.observe(viewLifecycleOwner) { devicesStatuses ->
            updateDeviceStatusUI(devicesStatuses)
        }

        viewModel.lastUpdatesList.observe(viewLifecycleOwner) { updatingList ->
            updateLastUpdateRecyclerView(updatingList)
        }

        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            updateUpdatingTextVisibility(isUpdating)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }
    }

    private fun updateDeviceStatusUI(devicesStatuses: DeviceStatuses) {
        with(binding) {
            allDevices.text = devicesStatuses.all
            activeDevices.text = devicesStatuses.active
            inActiveDevices.text = devicesStatuses.inActive
        }
    }

    private fun updateLastUpdateRecyclerView(updatingList: List<LastUpdateType>) {
        lastUpdateAdapter?.items = updatingList
        lastUpdateAdapter?.notifyDataSetChanged()
    }

    private fun updateUpdatingTextVisibility(isUpdating: Boolean) {
        if (isUpdating) {
            binding.shimmerHorizontalContainers.visibility = View.VISIBLE
            binding.shimmerRecycler.visibility = View.VISIBLE
            binding.lastUpdateRecycler.visibility = View.GONE
            binding.horizontalContainers.visibility = View.GONE
        } else {
            binding.shimmerHorizontalContainers.visibility = View.GONE
            binding.shimmerRecycler.visibility = View.GONE
            binding.lastUpdateRecycler.visibility = View.VISIBLE
            binding.horizontalContainers.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
