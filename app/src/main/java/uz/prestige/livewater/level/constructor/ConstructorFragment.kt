package uz.prestige.livewater.level.constructor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ConstructorFragmentBinding
import uz.prestige.livewater.level.constructor.adapter.ConstructorPagingAdapter
import uz.prestige.livewater.level.constructor.view_model.ConstructorViewModel
import uz.prestige.livewater.utils.toFormattedDate

class ConstructorFragment : Fragment(R.layout.constructor_fragment),
    FilterListener {

    private var _binding: ConstructorFragmentBinding? = null
    private val binding get() = _binding!!

    //    private var constructorAdapter: ConstructorAdapter? = null
    private val viewModel: ConstructorViewModel by viewModels()
    private var constructorAdapter: ConstructorPagingAdapter? = null

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
            val bottomSheetFragment =
                uz.prestige.livewater.level.constructor.FilterBottomSheetDialogFragment()
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
        this.constructorAdapter = ConstructorPagingAdapter()
        binding.constructorRecyclerView.apply {
            adapter = this@ConstructorFragment.constructorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        setupObservers()
    }

    private fun setupObservers() {
//        viewModel.getConstructor("", "", "all", "all")
//        viewModel.constructorList.observe(viewLifecycleOwner) {
//            this.constructorAdapter?.submitData(it)
//        }
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

        getConstructorData("", "", "all", "all")
    }

    private fun getConstructorData(
        startTime: String,
        endTime: String,
        regionId: String,
        deviceId: String
    ) {
        lifecycleScope.launch {
            viewModel.fetchConstructorData(startTime, endTime, regionId, deviceId)
                .flowOn(Dispatchers.IO).collectLatest {
//                constructorAdapter?.submitData(it)
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

//        viewModel.getConstructor(startTime, endTime, regionId, deviceId)

        getConstructorData(startTime, endTime, regionId, deviceId)

        Log.d("onApplyTag", "onApply: $regionId $deviceId")
    }
}