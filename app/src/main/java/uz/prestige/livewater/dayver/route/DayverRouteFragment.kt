package uz.prestige.livewater.dayver.route

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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentRouteBinding
import uz.prestige.livewater.utils.UiState
import uz.prestige.livewater.dayver.route.adapter.RoutePagingAdapter
import uz.prestige.livewater.dayver.route.view_model.DayverRouteViewModel
import uz.prestige.livewater.utils.toFormattedDate
import uz.prestige.livewater.utils.toFormattedTime

@AndroidEntryPoint
class DayverRouteFragment : Fragment(R.layout.fragment_route) {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DayverRouteViewModel by viewModels()
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
        observers() // Moved up to catch emissions immediately
        setupUI()
        getRouteData()
        swipeUp()
    }

    private fun setupUI() {
        setStatusBarColor()
        initRouteRecyclerView()
    }

    private fun swipeUp() {
        binding.swipeRefresh.setOnRefreshListener {
            getRouteData()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun initRouteRecyclerView() {
//        viewModel.getRouteList() // Call this after observers are set
        routeAdapter = RoutePagingAdapter { position ->
            val id = viewModel.getIdByPosition(position)
            viewModel.getBaseDataById(id)
        }

        with(binding.routeRecycler) {
            adapter = routeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun getRouteData() {
        lifecycleScope.launch {
            viewModel.fetchRouteList()
                .catch {
                    Snackbar.make(binding.swipeRefresh, "$it", Snackbar.LENGTH_LONG).show()
                }
                .flowOn(Dispatchers.IO)
                .collectLatest { pagingData ->
                    routeAdapter?.addLoadStateListener { loadState ->
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
                    routeAdapter?.submitData(pagingData.map {
                        viewModel.saveId(it.baseDataId)
                        it
                    })
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

    private fun observers() {


        viewModel.updatingState.observe(viewLifecycleOwner) { isUpdating ->
            binding.routeRecycler.visibility = if (isUpdating) View.GONE else View.VISIBLE
            binding.routeShimmer.visibility = if (isUpdating) View.VISIBLE else View.GONE
        }

        viewModel.updatingStateById.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), "Yuklanmoqda...", Snackbar.LENGTH_INDEFINITE).show()
        }

        viewModel.baseDataById.observe(viewLifecycleOwner) { baseData ->
            val snackBarMessage =
                "Level: ${baseData.level}, Salinity: ${baseData.salinity}, Temperature: ${baseData.temperature}, Date: ${baseData.date.toFormattedTime()} ${baseData.date.toFormattedDate()}"
            val snackBar = Snackbar.make(requireView(), snackBarMessage, Snackbar.LENGTH_SHORT)
            snackBar.setAction("Yopish") { snackBar.dismiss() }.show()
        }

        viewModel.error.observe(viewLifecycleOwner) { state ->
            val message = when (state) {
                is UiState.Error -> state.message
                else -> "Muvofaqiyatli"
            }
            val backgroundColor = when (state) {
                is UiState.Error -> R.color.redPrimary
                else -> R.color.greenPrimary
            }
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(requireContext().getColor(backgroundColor))
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

