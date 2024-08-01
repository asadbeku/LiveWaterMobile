package uz.prestige.livewater.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentRouteBinding
import uz.prestige.livewater.device.UiState
import uz.prestige.livewater.route.adapter.RouteAdapter
import uz.prestige.livewater.route.view_model.RouteViewModel
import uz.prestige.livewater.utils.toFormattedDate
import uz.prestige.livewater.utils.toFormattedTime

class RouteFragment : Fragment(R.layout.fragment_route) {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RouteViewModel by viewModels()
    private var routeAdapter: RouteAdapter? = null

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
        viewModel.getRouteList()
        routeAdapter = RouteAdapter { position ->
            viewModel.getBaseDataById(position)
        }

        with(binding.routeRecycler) {
            adapter = routeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observers() {

        viewModel.routeList.observe(viewLifecycleOwner) {
            routeAdapter?.items = it
        }

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

                is UiState.None -> {
                    context?.let {
                        Snackbar.make(
                            requireView(),
                            "Nomalum xabar",
                            Snackbar.LENGTH_SHORT
                        ).setBackgroundTint(it.getColor(R.color.darkGray)).show()
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

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}