package uz.prestige.livewater.device

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
import uz.prestige.livewater.device.adapter.DeviceAdapter
import uz.prestige.livewater.device.add_device.AddNewDeviceActivity
import uz.prestige.livewater.device.view_model.DeviceViewModel

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

    }

    private fun initLastUpdateRecyclerView() {

        viewModel.getDevices()

        deviceAdapter = DeviceAdapter {
            Snackbar.make(
                requireView(),
                "Pressed : ${viewModel.getDeviceId(it)}",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        with(binding.deviceRecycler) {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.devicesList.observe(viewLifecycleOwner) {
            deviceAdapter?.items = it
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