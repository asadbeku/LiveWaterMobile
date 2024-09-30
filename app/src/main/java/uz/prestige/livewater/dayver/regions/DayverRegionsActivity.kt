package uz.prestige.livewater.dayver.regions

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.ActivityRegionsBinding
import uz.prestige.livewater.dayver.regions.view_model.RegionsViewModel
import uz.prestige.livewater.level.constructor.type.RegionType
import uz.prestige.livewater.dayver.device.UiState
import uz.prestige.livewater.dayver.regions.adapter.ButtonClickListener
import uz.prestige.livewater.dayver.regions.adapter.RegionNameListener
import uz.prestige.livewater.dayver.regions.adapter.RegionsAdapter

class DayverRegionsActivity : AppCompatActivity(), ButtonClickListener, RegionNameListener {

    private lateinit var binding: ActivityRegionsBinding
    private val viewModel: RegionsViewModel by viewModels()
    private var regionsAdapter: RegionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        initList()
        observers()
    }

    private fun setupUI() {
        window.apply {
            statusBarColor = ContextCompat.getColor(context, R.color.colorPrimary)
            decorView.systemUiVisibility = 0
        }

        binding.fabButton.setOnClickListener {
            showEditRegionDialog(getString(R.string.add_region_title), "", null)
        }
    }

    private fun initList() {
        regionsAdapter = RegionsAdapter(this)

        with(binding.regionsRecycler) {
            adapter = regionsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun observers() {
        viewModel.regionsList.observe(this) { regions ->
            regionsAdapter?.submitList(regions)
        }

        viewModel.message.observe(this) { state ->
            val message = when (state) {
                is UiState.Error -> state.message
                is UiState.Success -> state.message
                else -> getString(R.string.default_message)
            }

            Snackbar.make(binding.regionsMainContainer, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(
                    ContextCompat.getColor(
                        this,
                        if (state is UiState.Error) R.color.redPrimary else R.color.greenPrimary
                    )
                )
                .show()
        }
    }

    private fun showEditRegionDialog(title: String, initialName: String, region: RegionType?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_region, null)
        val editText = dialogView.findViewById<EditText>(R.id.deviceNameEditText)
        editText.setText(initialName)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val editedName = editText.text.toString()

                handleRegionEdit(region?.id ?: "", editedName)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun handleRegionEdit(id: String, name: String) {
        if (name.isBlank()) {
            Snackbar.make(
                binding.regionsMainContainer,
                getString(R.string.invalid_name),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val json = JsonObject().apply { addProperty("name", name) }

        if (id.isBlank()) {
            viewModel.addRegion(json)
        } else {
            viewModel.updateRegion(id, json)
        }

        viewModel.getRegions(100)
    }

    override fun onEditButtonClick(onItemClick: Int) {
        val region = viewModel.getRegionByPosition(onItemClick)

        showEditRegionDialog(getString(R.string.edit_region_title), region.name, region)

        viewModel.getRegions(100)
    }

    override fun onRemoveButtonClick(onItemClick: Int) {
        val region = viewModel.getRegionByPosition(onItemClick)
        viewModel.removeRegion(region.id)

        viewModel.getRegions(100)
    }

    override fun onResume() {
        super.onResume()

        viewModel.getRegions(0)
    }

    override fun regionEditedName(name: String) {
        viewModel.getRegions(100)
    }
}
