package uz.prestige.livewater.dayver.constructor

import android.annotation.SuppressLint
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ConstructorFragmentBinding
import uz.prestige.livewater.dayver.constructor.adapter.ConstructorPagingAdapter
import uz.prestige.livewater.dayver.constructor.view_model.ConstructorViewModel
import uz.prestige.livewater.utils.toFormattedDate

@AndroidEntryPoint
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
                FilterBottomSheetDialogFragment()
            bottomSheetFragment.fListener = this@ConstructorFragment // Pass the listener reference
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.download -> {
                    viewModel.downloadExcel()
                    true
                }

                else -> {

                    false
                }
            }
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

        getConstructorData(null, null, null, null)
    }

    private fun getConstructorData(
        startTime: String?,
        endTime: String?,
        regionId: String?,
        deviceId: String?
    ) {
        lifecycleScope.launch {
            viewModel.fetchConstructorData(startTime, endTime, regionId, deviceId)
                .flowOn(Dispatchers.IO)
                .collectLatest { pagingData ->

                    pagingData.map {
                        Log.d("ConstructorFragment", "item: ${it.level}")
                    }

                    constructorAdapter?.let { adapter ->
                        adapter.addLoadStateListener { loadState ->
                            when (loadState.source.refresh) {
                                is LoadState.Error -> {
                                    val errorState = loadState.source.refresh as LoadState.Error
                                    Log.d(
                                        "ConstructorFragment",
                                        "Error: ${errorState.error.message}"
                                    )
                                    showErrorState()
                                }

                                is LoadState.Loading -> {
                                    Log.d("ConstructorFragment", "Loading")
                                    showLoadingState()
                                }

                                is LoadState.NotLoading -> {
                                    Log.d("ConstructorFragment", "Loaded")
                                    showContentState()
                                }
                            }
                        }

                        adapter.submitData(pagingData)
                    }
                }
        }
    }

    private fun showLoadingState() {
        with(binding) {
            shimmerRecycler.visibility = View.VISIBLE
            shimmerRecycler.startShimmer()
            emptyTextView.visibility = View.GONE
            constructorRecyclerView.visibility = View.GONE
        }
    }

    private fun showErrorState() {
        with(binding) {
            emptyTextView.visibility = View.VISIBLE
            constructorRecyclerView.visibility = View.GONE
            shimmerRecycler.visibility = View.GONE
            shimmerRecycler.stopShimmer()
        }
    }

    private fun showContentState() {
        with(binding) {
            emptyTextView.visibility = View.GONE
            constructorRecyclerView.visibility = View.VISIBLE
            shimmerRecycler.visibility = View.GONE
            shimmerRecycler.stopShimmer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onApply(
        startTime: String?,
        endTime: String?,
        regionId: String?,
        deviceId: String?
    ) {
        Log.d("onApplyTag", "onApply: $startTime $endTime $regionId $deviceId")
        binding.constructorDateTextView.text =
            if (startTime.isNullOrEmpty() || endTime.isNullOrEmpty()) {
                "Oxirgi yangilanishlar"
            } else {
                "${startTime.toLong().toFormattedDate()} - ${endTime.toLong().toFormattedDate()}"
            }

        viewModel.generateFilterUrl(startTime, endTime, regionId, deviceId)
        getConstructorData(
            startTime = startTime,
            endTime = endTime,
            regionId = regionId,
            deviceId = deviceId
        )

        Log.d("onApplyTag", "onApply: $regionId $deviceId")
    }
}