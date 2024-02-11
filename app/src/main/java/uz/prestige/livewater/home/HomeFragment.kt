package uz.prestige.livewater.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentHomeBinding
import uz.prestige.livewater.home.adapter.LastUpdatesAdapter
import uz.prestige.livewater.home.types.LastUpdateType
import uz.prestige.livewater.home.types.DeviceStatuses
import uz.prestige.livewater.home.view_model.HomeViewModel

class HomeFragment: Fragment(R.layout.fragment_home) {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private var lastUpdateAdapter: LastUpdatesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        setStatusBarColor()
        initLastUpdateRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDevicesStatusesAndLastUpdates()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun initLastUpdateRecyclerView() {
        lastUpdateAdapter = LastUpdatesAdapter()

        with(binding.lastUpdateRecycler) {
            adapter = lastUpdateAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            delay(800)
            viewModel.getDevicesStatusesAndLastUpdates()
        }

        viewModel.deviceStatuses.observe(viewLifecycleOwner) { devicesStatuses ->
            updateDeviceStatusUI(devicesStatuses)
        }

        viewModel.lastUpdatesList.observe(viewLifecycleOwner) { updatingList ->
            updateLastUpdateRecyclerView(updatingList)
        }

        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            updateUpdatingTextVisibility(isUpdating)
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
