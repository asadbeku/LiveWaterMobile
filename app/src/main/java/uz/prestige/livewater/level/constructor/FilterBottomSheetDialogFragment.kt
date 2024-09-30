package uz.prestige.livewater.level.constructor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.util.Pair
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import uz.prestige.livewater.R
import uz.prestige.livewater.databinding.FragmentFilterBinding
import uz.prestige.livewater.level.constructor.view_model.FilterViewModel
import uz.prestige.livewater.utils.toFormattedDate

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    internal var fListener: FilterListener? = null

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterViewModel by viewModels()

    private var regionId = "all"
    private var deviceId = "all"
    private var startTime = ""
    private var endTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.getRegions()
        viewModel.getDevices("all")

        viewModel.regionsLiveData.observe(viewLifecycleOwner) { regions ->
            setupRadioButtons(regions.map { it.name })
        }

        viewModel.serialNumbersLiveData.observe(viewLifecycleOwner) { dropDown(it) }
    }

    private fun setupClickListeners() {
        binding.applyButton.setOnClickListener {
            fListener?.onApply(startTime, endTime, deviceId, regionId)
            dismiss()
        }

        binding.cardViewStartTime.setOnClickListener { showTimePicker("Boshlanish vaqti") }
        binding.cardViewEndTime.setOnClickListener { showTimePicker("Tugalash vaqti") }
        binding.cardViewDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val dateRangePicker =

            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first   // Long type representing start date in milliseconds
            val endDate = selection.second   // Long type representing end date in milliseconds

            startTime = startDate.toString()
            endTime = endDate.toString()

            binding.dateTextView.text =
                "${startDate.toFormattedDate()} - ${endDate.toFormattedDate()}"
        }

        dateRangePicker.show(childFragmentManager, "DateRangePicker")
    }

    private fun showTimePicker(title: String) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(title)
            .build()

        picker.addOnPositiveButtonClickListener {
            val selectedHour = picker.hour.toString().padStart(2, '0')
            val selectedMinute = picker.minute.toString().padStart(2, '0')
            val selectedTime = "$selectedHour:$selectedMinute"

            if (title == "Boshlanish vaqti") {
                binding.startTimeTextView.text = selectedTime
            } else {
                binding.endTimeTextView.text = selectedTime
            }
        }

        picker.show(childFragmentManager, "TimePicker")
    }

    private fun setupRadioButtons(list: List<String>) {
        binding.radioGroup.removeAllViews()

        for (label in list) {
            val radioButton = RadioButton(requireContext()).apply {
                text = label
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT
                )
            }

            binding.radioGroup.addView(radioButton)
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = group.findViewById(checkedId)
            val selectedOption = radioButton.text.toString()

            regionId = viewModel.getRegionIdByRegionName(selectedOption)
            viewModel.getDevicesByRegionId(regionId)
        }
    }

    private fun dropDown(list: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_dropdown_item, list)
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = if (parent.getItemAtPosition(position)
                    .toString() == "Barcha qurilmalar"
            ) "all" else parent.getItemAtPosition(position).toString()
            deviceId = viewModel.getDevicesIdBySerialNumber(selectedItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}