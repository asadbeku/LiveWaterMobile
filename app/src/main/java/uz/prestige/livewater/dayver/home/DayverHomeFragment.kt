package uz.prestige.livewater.dayver.home

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
import uz.prestige.livewater.auth.LoginActivity
import uz.prestige.livewater.auth.TokenManager
import uz.prestige.livewater.databinding.FragmentDayverHomeBinding
import uz.prestige.livewater.dayver.home.adapter.LastUpdatesAdapterDayver
import uz.prestige.livewater.dayver.home.view_model.HomeDayverViewModel
import uz.prestige.livewater.dayver.types.LastUpdateTypeDayver
import uz.prestige.livewater.level.home.types.DeviceStatuses

class DayverHomeFragment : Fragment(R.layout.fragment_dayver_home) {

    private var _binding: FragmentDayverHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeDayverViewModel by viewModels()
    private var lastUpdateAdapter: LastUpdatesAdapterDayver? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayverHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        setupToolbarMenu()
    }

    private fun setupUI() {
        setStatusBarColor()
        initLastUpdateRecyclerView()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDevicesStatusesAndLastUpdates()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initLastUpdateRecyclerView() {
        lastUpdateAdapter = LastUpdatesAdapterDayver()
        binding.lastUpdateRecycler.apply {
            adapter = lastUpdateAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
            decorView.systemUiVisibility = 0
        }
    }

    private fun setupToolbarMenu() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exit -> {
                    TokenManager.clearToken(requireContext())
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            getDevicesStatusesAndLastUpdates()

            deviceStatuses.observe(viewLifecycleOwner) { updateDeviceStatusUI(it) }
            lastUpdatesList.observe(viewLifecycleOwner) { updateLastUpdateRecyclerView(it) }
            updatingState.observe(viewLifecycleOwner) { updateUpdatingTextVisibility(it) }
            error.observe(viewLifecycleOwner) { showError(it) }
        }
    }

    private fun updateDeviceStatusUI(deviceStatuses: DeviceStatuses) {
        binding.apply {
            allDevices.text = deviceStatuses.all
            activeDevices.text = deviceStatuses.active
            inActiveDevices.text = deviceStatuses.inActive
        }
    }

    private fun updateLastUpdateRecyclerView(updatingList: List<LastUpdateTypeDayver>) {
        lastUpdateAdapter?.apply {
            items = updatingList
            notifyDataSetChanged()
        }
    }

    private fun updateUpdatingTextVisibility(isUpdating: Boolean) {
        binding.apply {
            if (isUpdating) {
                shimmerHorizontalContainers.visibility = View.VISIBLE
                shimmerRecycler.visibility = View.VISIBLE
                lastUpdateRecycler.visibility = View.GONE
                horizontalContainers.visibility = View.GONE
            } else {
                shimmerHorizontalContainers.visibility = View.GONE
                shimmerRecycler.visibility = View.GONE
                lastUpdateRecycler.visibility = View.VISIBLE
                horizontalContainers.visibility = View.VISIBLE
            }
        }
    }

    private fun showError(error: String) {
        Snackbar.make(requireView(), "Error: $error", Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
