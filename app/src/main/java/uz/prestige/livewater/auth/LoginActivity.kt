package uz.prestige.livewater.auth

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.BuildConfig
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.prestige.livewater.MainActivity
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityLoginBinding
import uz.prestige.livewater.auth.view_model.LoginViewModel
import uz.prestige.livewater.auth.view_model.ValidTokenResponse
import uz.prestige.livewater.dayver.network.NetworkDayver
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.utils.UiState

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var accountType = "level"
    private var animator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NetworkLevel.setContext(applicationContext)
        NetworkDayver.setContext(applicationContext)

        setupObservers()
        setupLoginButton()
        viewModel.isTokenValid()

        binding.changeAccountButton.setOnClickListener { toggleAccountType() }

        binding.usernameEditText.editText?.setText("asadbekman")
        binding.passwordEditText.editText?.setText("20010827Aa")
    }

    private fun toggleAccountType() {
        accountType = if (accountType == "level") {
            animateText("Dayver")
            binding.changeAccountButton.text = "Level"

            if (!BuildConfig.DEBUG) {
                binding.usernameEditText.editText?.setText("admin")
                binding.passwordEditText.editText?.setText("Newadmin")
            }
            "dayver"
        } else {
            animateText("Level")
            binding.changeAccountButton.text = "Dayver"

            if (!BuildConfig.DEBUG) {
                binding.usernameEditText.editText?.setText("asadbekman")
                binding.passwordEditText.editText?.setText("20010827Aa")
            }
            "level"
        }
    }

    private fun animateText(text: String) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, text.length).apply {
            duration = 1000
            addUpdateListener { animation ->
                binding.appNameTextView.text = text.substring(0, animation.animatedValue as Int)
            }
            start()
        }
    }

    private fun setupObservers() {
        viewModel.apply {

            status.observe(this@LoginActivity) { state ->
                when (state) {
                    is UiState.Success -> handleSuccess(state.data!!)
                    is UiState.Error -> showSnackbar(state.message, R.color.redPrimary)
                    is UiState.Loading -> showSnackbar("Loading...", R.color.darkGray)
                }
            }

            updatingState.observe(this@LoginActivity, ::updateUiState)
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener { checkAuth() }

    }

    private fun checkAuth() {
        val login = binding.usernameEditText.editText?.text.toString().trim()
        val password = binding.passwordEditText.editText?.text.toString()

        when {
            login.isEmpty() || password.isEmpty() -> showSnackbar(
                "Username or password is empty!",
                R.color.redPrimary
            )

            login.length < 3 || password.length < 3 -> showSnackbar(
                "Username or password is too short!",
                R.color.redPrimary
            )

            else -> viewModel.checkAuth(login, password, accountType)
        }
    }

    private fun updateUiState(isUpdating: Boolean) {
        binding.loginButton.apply {
            isEnabled = !isUpdating
            text = if (isUpdating) "Loading..." else "Login"
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    private fun showSnackbar(message: String, @ColorRes backgroundColor: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, backgroundColor))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    private fun navigateToMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            startActivity(it)
            finish()
        }
    }

    private fun handleSuccess(data: Any) {

        Log.d("LoginActivity", "handleSuccess: $data")

        val message = if (data is String){
            data as String
        }else{
            (data as ValidTokenResponse).message
        }

        when (message) {

            "Valid token" -> navigateToMainActivity()
            "Login successful" -> {
                showSnackbar("Login successful!", R.color.greenPrimary)
                navigateToMainActivity()
            }

            else -> showSnackbar("Incorrect login or password!", R.color.redPrimary)
        }

//        if (data.data.success && data.data.statusCode == 200) {
//            navigateToMainActivity()
//        }
    }
}


