package uz.prestige.livewater.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.prestige.livewater.MainActivity
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityLoginBinding
import uz.prestige.livewater.login.view_model.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupLoginButton()
    }

    private fun setupObservers() {
        viewModel.apply {
            status.observe(this@LoginActivity) { isSuccessful ->
                val message = if (isSuccessful) "Kirish muvaffaqiyatli amalga oshirildi!" else "Username yoki parol xato!"
                val backgroundColor = if (isSuccessful) R.color.greenPrimary else R.color.redPrimary
                showSnackbar(message, backgroundColor)
                if (isSuccessful) startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }

            errorMessage.observe(this@LoginActivity) { errorMessage ->
                showSnackbar(errorMessage, R.color.redPrimary)
            }

            updatingState.observe(this@LoginActivity, ::updateUiState)
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            checkAuth()
        }
    }

    private fun checkAuth() {
        val login = binding.usernameEditText.editText?.text.toString().trim()
        val password = binding.passwordEditText.editText?.text.toString()

        when {
            login.isEmpty() || password.isEmpty() -> showSnackbar("Username yoki parol kiritilmadi!", R.color.redPrimary)
            login.length < 3 || password.length < 3 -> showSnackbar("Username yoki parol juda qisqa!", R.color.redPrimary)
            else -> viewModel.checkAuth(login, password)
        }
    }

    private fun updateUiState(isUpdating: Boolean) {
        with(binding.loginButton) {
            isEnabled = !isUpdating
            text = if (isUpdating) "Yuklanmoqda..." else "Login"
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    private fun showSnackbar(message: String, @ColorRes backgroundColor: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, backgroundColor))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }
}