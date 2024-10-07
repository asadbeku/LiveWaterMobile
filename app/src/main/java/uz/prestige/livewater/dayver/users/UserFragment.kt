package uz.prestige.livewater.dayver.users

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentUserBinding
import uz.prestige.livewater.dayver.users.adapter.UsersPagingAdapter
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.dayver.users.view_model.UsersViewModel

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsersViewModel by viewModels()

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
        observers()
        bindUiState()
        getUsers()
    }

    private fun bindUiState() {
        binding.addUserButton.setOnClickListener {
            val intent = Intent(requireContext(), AddUserActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            getUsers()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor = getColor(requireContext(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun changeUser(userInfo: DayverUserType) {
        Log.d("userInfo", "changeUser: $userInfo")
        val intent = Intent(requireContext(), AddUserActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable(
            "userInfo",
            userInfo
        )
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun initUsersRecycler() {
        userAdapter = UsersPagingAdapter {
            val userId = viewModel.getUserId(it)
            viewModel.getUserDataById(userId)
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

    private fun getUsers() {
        lifecycleScope.launch {
            viewModel.fetchUsers()
                .catch {
                    showMessage("Problem: ${it.message}", R.color.redPrimary)
                }
                .flowOn(Dispatchers.IO)
                .collectLatest {

                    userAdapter?.addLoadStateListener { loadState ->
                        when (loadState.source.refresh) {
                            is LoadState.Loading -> {
                                showLoadingState()
                            }

                            is LoadState.Error -> {
                                val errorState = loadState.source.refresh as LoadState.Error
                                showErrorState(errorState.error.message.toString())
                            }

                            is LoadState.NotLoading -> {
                                showContentState()
                            }
                        }
                    }

                    userAdapter?.submitData(
                        it.map { userInfo ->
                            viewModel.saveUserId(userInfo.id)
                            userInfo
                        }
                    )
                }
        }
    }

    private fun showLoadingState() {
        binding.shimmerRecycler.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
        binding.emptyTextView.visibility = View.GONE
        binding.usersRecycler.visibility = View.GONE
    }

    private fun showErrorState(message: String) {
        binding.shimmerRecycler.stopShimmer()
        binding.shimmerRecycler.visibility = View.GONE
        binding.emptyTextView.visibility = View.VISIBLE
        binding.usersRecycler.visibility = View.GONE

        Snackbar.make(binding.swipeRefresh, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showContentState() {
        binding.shimmerRecycler.stopShimmer()
        binding.shimmerRecycler.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
        binding.usersRecycler.visibility = View.VISIBLE
    }

    private fun observers() {
        viewModel.userData.observe(viewLifecycleOwner) {
            changeUser(it)
        }

    }
}