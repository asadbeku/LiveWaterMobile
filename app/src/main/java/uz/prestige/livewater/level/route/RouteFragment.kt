package uz.prestige.livewater.level.route

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentRouteBinding
import uz.prestige.livewater.level.constructor.adapter.ConstructorPagingAdapter
import uz.prestige.livewater.level.route.adapter.RouteAdapter
import uz.prestige.livewater.level.route.adapter.RoutePagingAdapter
import uz.prestige.livewater.level.route.view_model.RouteViewModel
import uz.prestige.livewater.utils.UiState
import uz.prestige.livewater.utils.toFormattedDate
import uz.prestige.livewater.utils.toFormattedTime

@AndroidEntryPoint
class RouteFragment : Fragment(R.layout.fragment_route) {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RouteViewModel by viewModels()
    private var routeAdapter: RoutePagingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        setupUI()
        swipeUp()
        getRouteData()
    }

    private fun swipeUp() {
        binding.swipeRefresh.setOnRefreshListener {
            getRouteData()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupUI() {
        setStatusBarColor()
        initRouteRecyclerView()
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun initRouteRecyclerView() {
        this.routeAdapter = RoutePagingAdapter { position ->
            val id = viewModel.getRouteIdByPosition(position)
            viewModel.getBaseDataById(id)
        }
        binding.routeRecycler.apply {
            adapter = this@RouteFragment.routeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observers() {
        viewModel.updatingState.observe(viewLifecycleOwner) {
            viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
                if (isUpdating) {
                    binding.routeRecycler.visibility = View.GONE
                    binding.routeShimmer.visibility = View.VISIBLE
                } else {
                    binding.routeRecycler.visibility = View.VISIBLE
                    binding.routeShimmer.visibility = View.GONE
                }
            }
        }

        viewModel.updatingStateById.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), "Yuklanmoqda...", 16000).show()
        }

        viewModel.baseDataById.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireView(),
                "Level: ${it.level}, Volume: ${it.volume}, Date:${it.date.toFormattedTime()} ${it.date.toFormattedDate()}",
                7000
            )
            snackBar.setAction("Yopish") {
                snackBar.dismiss()
            }.show()
        }

        viewModel.error.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Error -> {
                    context?.let {
                        Snackbar.make(
                            requireView(),
                            state.message,
                            Snackbar.LENGTH_LONG
                        ).setBackgroundTint(it.getColor(R.color.redPrimary)).show()
                    }
                }

                else -> {
                    context?.let {
                        Snackbar.make(
                            requireView(),
                            "Muvofaqiyatli",
                            Snackbar.LENGTH_SHORT
                        ).setBackgroundTint(it.getColor(R.color.greenPrimary)).show()
                    }
                }
            }
        }

    }

    private fun getRouteData() {
        lifecycleScope.launch {
            viewModel.fetchRouteData().flowOn(Dispatchers.IO).collectLatest { pagingData ->
                routeAdapter?.let { adapter ->
                    adapter.addLoadStateListener { loadState ->
                        when (loadState.source.refresh) {
                            is LoadState.Error -> {
                                val errorState = loadState.source.refresh as LoadState.Error
                                Log.d(
                                    "RouteFragment",
                                    "Error: ${errorState.error.message}"
                                )
                                showErrorState()
                            }

                            is LoadState.Loading -> {
                                Log.d("RouteFragment", "Loading")
                                showLoadingState()
                            }

                            is LoadState.NotLoading -> {
                                Log.d("RouteFragment", "Loaded")
                                showContentState()
                            }
                        }
                    }
                    adapter.submitData(pagingData.map {
                        viewModel.saveRouteId(it.baseDataId)
                        it
                    })
                }
            }
        }
    }

    private fun showLoadingState() {
        with(binding) {
            routeShimmer.visibility = View.VISIBLE
            routeShimmer.startShimmer()
            emptyTextView.visibility = View.GONE
            routeRecycler.visibility = View.GONE
        }
    }

    private fun showErrorState() {
        with(binding) {
            emptyTextView.visibility = View.VISIBLE
            routeRecycler.visibility = View.GONE
            routeShimmer.visibility = View.GONE
            routeShimmer.stopShimmer()
        }
    }

    private fun showContentState() {
        with(binding) {
            emptyTextView.visibility = View.GONE
            routeRecycler.visibility = View.VISIBLE
            routeShimmer.visibility = View.GONE
            routeShimmer.stopShimmer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}