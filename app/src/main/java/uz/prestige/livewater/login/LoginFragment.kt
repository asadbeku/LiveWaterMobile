package uz.prestige.livewater.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.prestige.livewater.MainActivity
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentLoginBinding
import uz.prestige.livewater.home.HomeFragment
import uz.prestige.livewater.login.view_model.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        observers()

        binding.loginButton.setOnClickListener {
            val login = binding.usernameEditText.editText?.text.toString()
            val password = binding.passwordEditText.editText?.text.toString()

//            viewModel.checkAuth(login, password)
            viewModel.checkAuth("Asadbekman", "20010827Aa")
        }

    }

    private fun observers() {
        viewModel.status.observe(this) { isSuccessful ->
            if (isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(
                    findViewById(R.id.mainContainer),
                    "Username yoki parol xato!",
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(getColor(R.color.redPrimary))
                    .setTextColor(getColor(R.color.white))
                    .show()
//                binding.usernameEditText.editText?.error = "Username yoki parol xato!"
            }
        }
    }

}