package uz.prestige.livewater.level.users

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentUserBinding
import uz.prestige.livewater.level.users.adapter.UsersAdapter
import uz.prestige.livewater.level.users.view_model.UsersViewModel
import uz.prestige.livewater.level.users.types.UserType

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsersViewModel by viewModels()

    private var userAdapter: UsersAdapter? = null

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
    }

    private fun bindUiState() {
        binding.addUserButton.setOnClickListener {
            val intent = Intent(requireContext(), AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor = getColor(requireContext(), R.color.colorPrimary)
        requireActivity().window.decorView.systemUiVisibility = 0
    }

    private fun changeUser(position: Int) {
        val id = viewModel.getDeviceId(position)
        val userInfo: UserType = viewModel.getDeviceDataById(id)!!

        val intent = Intent(requireContext(), AddUserActivity::class.java)

        // Create a bundle to pass data to the activity
        val bundle = Bundle()
        bundle.putParcelable("userInfo", userInfo) // Assuming uz.prestige.livewater.constructor.type.DeviceType is Parcelable

        // Put the bundle into the intent
        intent.putExtra("bundle", bundle)

        // Start the activity
        startActivity(intent)
    }

    private fun initUsersRecycler() {
        userAdapter = UsersAdapter {
            showMessage("Pressed: $it", R.color.greenPrimary)
            changeUser(it)
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

    override fun onStart() {
        super.onStart()
        viewModel.getUsers()

    }

    private fun observers() {
//        viewModel.getUsers()
        lifecycleScope.launch {
            viewModel.usersList
                .catch {
                    showMessage("Problem: ${it.message}", R.color.redPrimary)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    userAdapter?.items = it
                }
        }

    }
}