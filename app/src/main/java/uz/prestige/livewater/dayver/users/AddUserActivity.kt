package uz.prestige.livewater.dayver.users

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityAddUserBinding
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.dayver.users.view_model.AddUserViewModel
import uz.prestige.livewater.utils.UiState

@AndroidEntryPoint
class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private val viewModel: AddUserViewModel by viewModels()
    private var regionsList = listOf<uz.prestige.livewater.dayver.constructor.type.RegionType>()
    private var userInfo: DayverUserType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        bindObservers()

        NetworkLevel.setContext(applicationContext)
        getFromBundle()
    }

    private fun getFromBundle() {
        val bundle = intent.getBundleExtra("bundle")
        userInfo = bundle?.getParcelable("userInfo")

        binding.apply {
            name.setText(userInfo?.firstName)
            surname.setText(userInfo?.lastName)
            username.setText(userInfo?.username)
            region.editText?.setText(viewModel.getRegionName("", regionsList))
            roleEditText.editText?.setText(userInfo?.role)
            Log.d(
                "checkPhoneNumber",
                "PhoneNumber: ${phoneNumberEditText.editText?.text}, ${phoneNumberEditText.editText?.text?.isBlank()}"
            )
            if (phoneNumberEditText.editText?.text?.isBlank() == true) {
                phoneNumberEditText.visibility = View.GONE
                phoneNumberTextView.visibility = View.GONE
            } else {
                phoneNumberEditText.visibility = View.VISIBLE
                phoneNumberTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViews() {
        binding.addUserButton.setOnClickListener {
            if (isValidFields()) {
                if (!userInfo?.id.isNullOrBlank()) {
                    viewModel.changeUserInfo(userInfo!!.id, getUserJson())
                } else {
                    viewModel.addUser(getUserJson())
                }

            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.remove_user -> {
//                    Need remove user if have id in bundle
                    if (userInfo?.id.isNullOrBlank()) {
                        Snackbar.make(
                            binding.addUserContainer,
                            "Siz hali mavjud bo'lmagan foydalanuvchini o'chira olmaysiz",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        userInfo?.id?.let { it1 -> viewModel.removeUser(it1) }
                    }

                    true
                }

                else -> false

            }
        }
        setupDropdowns()


    }

    private fun setupDropdowns() {
        val roles = listOf("admin", "operator")
        binding.roleAutoComplete.setAdapter(bindDropdownAdapter(roles))

        lifecycleScope.launch {
            viewModel.regionsFlow.collectLatest { regions ->
                val regionNames = regions.map { it.name }
                regionsList = regions
                binding.regionAutoComplete.setAdapter(bindDropdownAdapter(regionNames))
            }
        }
    }

    private fun bindDropdownAdapter(list: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(this, R.layout.custom_dropdown_item, list)
    }

    private fun isValidFields(): Boolean {
        val fields = listOf(
            binding.nameEditText to "Ismi talab qilinadi",
            binding.surnameEditText to "Familyasi talab qilinadi",
            binding.usernameEditText to "Username talab qilinadi",
            binding.phoneNumberEditText to "Telefon raqami talab qilinadi",
            binding.passwordEditText to "Parolni kiritilishi kerak",
            binding.region to "Reginni tanlash talab qilinadi",
            binding.roleEditText to "Rolni tanlash"
        )

        fields.forEach { (textInput, errorMessage) ->
            if (textInput.editText?.text.isNullOrBlank()) {
                textInput.error = errorMessage
                return false
            } else {
                textInput.error = null
            }
        }
        return true
    }

    private fun getUserJson(): JsonObject {
        val regionName = binding.regionAutoComplete.text.toString()
        val regionId = viewModel.getRegionId(regionName, regionsList)

        Log.d("checkRegionId", "ID: $regionId")

        val userJson = JsonObject().apply {
            addProperty("first_name", binding.nameEditText.editText?.text.toString())
            addProperty("last_name", binding.surnameEditText.editText?.text.toString())
            addProperty("username", binding.usernameEditText.editText?.text.toString())
            addProperty(
                "mobil_phone",
                "+998" + binding.phoneNumberEditText.editText?.text.toString()
            )
            addProperty("password", binding.passwordEditText.editText?.text.toString())
            addProperty("region", regionId)
            addProperty("role", binding.roleAutoComplete.text.toString())
        }

        return userJson
    }

    override fun onResume() {
        super.onResume()

        viewModel.getRegions()
    }

    private fun bindObservers() {

        viewModel.finish.observe(this@AddUserActivity) {
            if (it) {
                finish()
            }
        }

        viewModel.message.observe(this@AddUserActivity) { state ->
            when (state) {
                is UiState.Error -> {

                    lifecycleScope.launch {
                        Snackbar.make(
                            binding.addUserContainer,
                            state.message,
                            Snackbar.LENGTH_LONG
                        ).setBackgroundTint(getColor(R.color.redPrimary)).show()
                    }

                }

                is UiState.Success -> {
                    var message = ""
                    if (state.data.toString() == "Changed") {
                        message = "Muvofaqiyatli o'zgartirildi"
                    }
                    Snackbar.make(
                        binding.addUserContainer,
                        message,
                        Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(getColor(R.color.greenPrimary)).show()

                }

                else -> {
                    Snackbar.make(
                        binding.addUserContainer,
                        "Xatolik mavjud",
                        Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(getColor(R.color.darkGray)).show()
                }
            }


        }

    }
}
