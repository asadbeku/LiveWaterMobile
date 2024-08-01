package uz.prestige.livewater.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import uz.prestige.livewater.MainActivity
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityLoginBinding
import uz.prestige.livewater.device.UiState
import uz.prestige.livewater.login.view_model.LoginViewModel
import uz.prestige.livewater.network.ApiService
import uz.prestige.livewater.network.Network

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Network.setContext(applicationContext)

        setupObservers()
        setupLoginButton()

        binding.test.setOnClickListener {
            lifecycleScope.launch {
                val response = Network.buildService(ApiService::class.java)
                    .isValidToken()

                Log.d("checkBearer", "Network: ${response.body()}")
            }

        }
    }

    private fun setupObservers() {
        viewModel.apply {
            status.observe(this@LoginActivity) { isSuccessful ->
                val message =
                    if (isSuccessful) "Login successful!" else "Incorrect username or password!"
                val backgroundColor = if (isSuccessful) R.color.greenPrimary else R.color.redPrimary
                showSnackbar(message, backgroundColor)
                if (isSuccessful) navigateToMainActivity()
            }

            error.observe(this@LoginActivity) { state ->
                when (state) {
                    is UiState.Success -> handleSuccessState(state)
                    is UiState.Error -> handleErrorState(state)
                    is UiState.None -> showSnackbar("Unknown message", R.color.darkGray)
                }
            }

            updatingState.observe(this@LoginActivity, ::updateUiState)
        }
    }

    private fun setupLoginButton() {
        viewModel.isTokenValid(applicationContext)

        binding.loginButton.setOnClickListener { checkAuth() }
    }

    private fun checkAuth() {
        val login = binding.usernameEditText.editText?.text.toString().trim()
        val password = binding.passwordEditText.editText?.text.toString()

        when {
            login.isEmpty() || password.isEmpty() -> showSnackbar(
                "Username or password is empty!", R.color.redPrimary
            )

            login.length < 3 || password.length < 3 -> showSnackbar(
                "Username or password is too short!", R.color.redPrimary
            )

            else -> viewModel.checkAuth(applicationContext, login, password)
        }
    }

    private fun updateUiState(isUpdating: Boolean) {
        with(binding.loginButton) {
            isEnabled = !isUpdating
            text = if (isUpdating) "Loading..." else "Login"
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    private fun showSnackbar(message: String, @ColorRes backgroundColor: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, backgroundColor))
            .setTextColor(ContextCompat.getColor(this, R.color.white)).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun handleSuccessState(state: UiState.Success) {
        var msg = ""

        if (state.message == "Success bearer") {
            navigateToMainActivity()
        }

        if (state.message == "Login") {
            msg = "Login successful!"
            navigateToMainActivity()
        } else msg =
            "Incorrect username or password!"


        Snackbar.make(
            binding.mainContainer,
            msg,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(getColor(R.color.greenPrimary)).show()
    }

    private fun handleErrorState(state: UiState.Error) {
        Snackbar.make(
            binding.mainContainer, state.message, Snackbar.LENGTH_LONG
        ).setBackgroundTint(getColor(R.color.redPrimary)).show()

        if (state.message == "Token eskirgan") {
            binding.authContainer.visibility = View.GONE
            binding.nestedScroll.visibility = View.VISIBLE
        }
    }
}
