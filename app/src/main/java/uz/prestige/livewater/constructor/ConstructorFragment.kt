package uz.prestige.livewater.constructor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uz.prestige.livewater.R
import uz.prestige.livewater.constructor.adapter.ConstructorAdapter
import uz.prestige.livewater.constructor.view_model.ConstructorViewModel
import uz.prestige.livewater.databinding.ConstructorFragmentBinding
import uz.prestige.livewater.utils.toFormattedDate

class ConstructorFragment : Fragment(R.layout.constructor_fragment), FilterListener {

    private var _binding: ConstructorFragmentBinding? = null
    private val binding get() = _binding!!

    private var constructorAdapter: ConstructorAdapter? = null
    private val viewModel: ConstructorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ConstructorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setStatusBarColor()
        initLastUpdateRecyclerView()

        binding.filterButton.setOnClickListener {
            val bottomSheetFragment = FilterBottomSheetDialogFragment()
            bottomSheetFragment.fListener = this // Pass the listener reference
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun initLastUpdateRecyclerView() {
        setupObservers()

        constructorAdapter = ConstructorAdapter()
        binding.constructorRecyclerView.apply {
            adapter = constructorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.getConstructor("", "", "all", "all")
        viewModel.constructorList.observe(viewLifecycleOwner) {
            constructorAdapter?.items = it
        }
        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            if (isUpdating) {
                binding.shimmerRecycler.visibility = View.VISIBLE
                binding.shimmerRecycler.startShimmer()
                binding.mainContainer.visibility = View.GONE
            } else {
                binding.shimmerRecycler.stopShimmer()
                binding.shimmerRecycler.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onApply(
        startTime: String,
        endTime: String,
        regionId: String,
        deviceId: String
    ) {
        Log.d("onApplyTag", "onApply: $startTime $endTime $regionId $deviceId")
        if (startTime == "all" || endTime == "all") {
            binding.constructorDateTextView.text = "Oxirgi yangilanishlar"
        } else if (startTime.isEmpty() || endTime.isEmpty()) {
            binding.constructorDateTextView.text = "Oxirgi yangilanishlar"
        } else {
            binding.constructorDateTextView.text =
                "${startTime.toLong().toFormattedDate()} - ${endTime.toLong().toFormattedDate()}"
        }

        viewModel.getConstructor(startTime, endTime, regionId, deviceId)
        Log.d("onApplyTag", "onApply: $regionId $deviceId")
    }
}