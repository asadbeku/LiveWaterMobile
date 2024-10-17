package uz.prestige.livewater.level.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
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
import uz.prestige.livewater.databinding.FragmentUserBinding
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.level.users.adapter.UsersAdapter
import uz.prestige.livewater.level.users.adapter.UsersPagingAdapter
import uz.prestige.livewater.level.users.view_model.UsersViewModel
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.level.users.view_model.UsersRepository

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UsersViewModel>()

    private var userAdapter: UsersPagingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarColor()
        initUsersRecycler()
        bindUiState()
        fetchUsers()
        observer()
    }

    private fun bindUiState() {
        binding.addUserButton.setOnClickListener {
            val intent = Intent(requireContext(), AddUserActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            fetchUsers()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor = getColor(requireContext(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun changeUser(userInfo: UserType) {

        val intent = Intent(requireContext(), AddUserActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable(
            "userInfo",
            userInfo
        )

        // Put the bundle into the intent
        intent.putExtra("bundle", bundle)

        // Start the activity
        startActivity(intent)
    }

    private fun initUsersRecycler() {
        userAdapter = UsersPagingAdapter {
            val id = viewModel.getUserIdByPosition(it)
            Log.d("UserFragment", "User ID: $id, position: $it")
            viewModel.getUserInfoById(id)
        }

        with(binding.usersRecycler) {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showMessage(message: String, color: Int) {
        Snackbar.make(
            binding.mainContainerUser, message, Snackbar.LENGTH_SHORT
        ).setBackgroundTint(getColor(requireContext(), color)).show()
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            viewModel.fetchUsersData()
                .flowOn(Dispatchers.IO)
                .collectLatest { pagingData ->
                    userAdapter?.submitData(pagingData.map {
                        viewModel.saveUserId(it.id)
                        it
                    })
                }

            userAdapter?.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.Error -> {
                        val errorState = loadState.source.refresh as LoadState.Error
                        Log.d("DeviceFragment", "Error: ${errorState.error.message}")
                        showErrorState()
                    }

                    is LoadState.Loading -> {
                        Log.d("DeviceFragment", "Loading")
                        showLoadingState()
                    }

                    is LoadState.NotLoading -> {
                        Log.d("DeviceFragment", "Loaded")
                        showContentState()
                    }
                }
            }
        }
    }

    private fun observer() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            Log.d("UserFragment", "Received user data: $it")
            changeUser(it)
        }
    }

    private fun showLoadingState() {
        binding.shimmerRecycler.visibility = View.VISIBLE
        binding.shimmerRecycler.startShimmer()
        binding.emptyTextView.visibility = View.GONE
        binding.usersRecycler.visibility = View.GONE
    }

    private fun showErrorState() {
        binding.shimmerRecycler.stopShimmer()
        binding.shimmerRecycler.visibility = View.GONE
        binding.emptyTextView.visibility = View.VISIBLE
        binding.usersRecycler.visibility = View.GONE
    }

    private fun showContentState() {
        binding.shimmerRecycler.stopShimmer()
        binding.shimmerRecycler.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
        binding.usersRecycler.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}