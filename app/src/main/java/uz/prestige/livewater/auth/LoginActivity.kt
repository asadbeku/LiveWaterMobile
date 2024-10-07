package uz.prestige.livewater.auth

import android.animation.ValueAnimator
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
import uz.prestige.livewater.level.device.UiState
import uz.prestige.livewater.auth.view_model.LoginViewModel
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.NetworkDayver
import uz.prestige.livewater.level.network.NetworkLevel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var accountType = "level"
    private var textToAnimate: String = ""
    private var animator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NetworkLevel.setContext(applicationContext)
        NetworkDayver.setContext(applicationContext)

        setupObservers()
        setupLoginButton()
        checkToken()

        binding.test.setOnClickListener {
            lifecycleScope.launch {
                val responseLevel = NetworkLevel.buildService(ApiService::class.java)
                    .isValidToken()
                val responseDayver =
                    NetworkDayver.buildService(ApiService::class.java).isValidToken()

                Log.d("checkBearer", "Network: ${responseLevel.body()}")
                Log.d("checkBearer", "Network: ${responseDayver.body()}")
            }
        }
        binding.changeAccountButton.setOnClickListener {
            changeAccountType()
        }
    }

    private fun checkToken() {
        viewModel.isTokenValid(applicationContext)
    }

    private fun changeAccountType() {

        accountType = if (accountType == "level") {
//            binding.appNameTextView.text = "Live water - Dayver"
            animateText("Dayver")
            binding.changeAccountButton.text = "Level"
            "dayver"
        } else {
//            binding.appNameTextView.text = "Live water - Level"
            animateText("Level")
            binding.changeAccountButton.text = "Dayver"
            "level"
        }
    }

    private fun animateText(text: String) {
        textToAnimate = text

        animator?.let { it.cancel() }

        animator = ValueAnimator.ofInt(0, textToAnimate.length)
        animator?.duration = 1000
        animator?.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            binding.appNameTextView.text = textToAnimate.substring(0, animatedValue)
        }
        animator?.start()
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
//        viewModel.isTokenValid(applicationContext)

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

            else -> viewModel.checkAuth(applicationContext, login, password, accountType)
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
            "Username yoki parol xato!"

        if (state.message == "unsuccessful") {
            msg = "Token eskirgan"
        }

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
