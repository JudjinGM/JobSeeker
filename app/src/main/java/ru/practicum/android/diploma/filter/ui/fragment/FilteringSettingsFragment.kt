package ru.practicum.android.diploma.filter.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.custom_view.ButtonWithSelectedValues
import ru.practicum.android.diploma.common.custom_view.model.ButtonWithSelectedValuesState
import ru.practicum.android.diploma.databinding.FragmentFilteringSettingsBinding
import ru.practicum.android.diploma.filter.ui.model.ButtonState
import ru.practicum.android.diploma.filter.ui.model.ClearFieldButtonNavigationState
import ru.practicum.android.diploma.filter.ui.model.FilterFieldsState
import ru.practicum.android.diploma.filter.ui.viewModel.FilteringSettingsViewModel

class FilteringSettingsFragment : Fragment() {

    private var _binding: FragmentFilteringSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FilteringSettingsViewModel>()
    private var salary = BLANK_STRING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteringSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClicks()

        initObservers()

        initListeners()

        viewModel.updateStates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initOnClicks() {
        binding.areaCustomView.onButtonClick {
            if (viewModel.observeAreaState().value is FilterFieldsState.Content) {
                viewModel.clearAreaButtonClicked()
                binding.selectedEnterTheAmountTextInputEditText.clearFocus()
            } else {
                findNavController().navigate(R.id.action_filteringSettingsFragment_to_filteringChoosingWorkplaceFragment)
            }

        }
        binding.areaCustomView.setOnClickListener {
            findNavController().navigate(R.id.action_filteringSettingsFragment_to_filteringChoosingWorkplaceFragment)
        }

        binding.industryCustomView.onButtonClick {
            if (viewModel.observeIndustryState().value is FilterFieldsState.Content) {
                viewModel.clearIndustryButtonClicked()
                binding.selectedEnterTheAmountTextInputEditText.clearFocus()
            } else {
                findNavController().navigate(R.id.action_filteringSettingsFragment_to_filteringSectorFragment)
            }
        }
        binding.industryCustomView.setOnClickListener {
            findNavController().navigate(R.id.action_filteringSettingsFragment_to_filteringSectorFragment)
        }

        binding.filteringSettingsToolbar.setNavigationOnClickListener {
            viewModel.clearTempFilterOptions()
            findNavController().popBackStack()
        }

        binding.enterTheAmountTextInputLayout.setEndIconOnClickListener {
            viewModel.clearSalary()
            viewModel.updateButtonsStates()
            binding.selectedEnterTheAmountTextInputEditText.clearFocus()
            setTextInputLayoutHintColor(binding.enterTheAmountTextInputLayout, requireContext(), R.color.gray_white)
        }

        binding.filteringSettingsOnlyWithSalaryCheckbox.setOnClickListener {
            viewModel.setOnlyWithSalary(
                binding.filteringSettingsOnlyWithSalaryCheckbox.isChecked
            )
            viewModel.updateButtonsStates()
            binding.selectedEnterTheAmountTextInputEditText.clearFocus()
        }

        binding.resetButton.setOnClickListener {
            viewModel.clearAll()
            viewModel.updateStates()
            binding.selectedEnterTheAmountTextInputEditText.clearFocus()
        }

        binding.applyButton.setOnClickListener {
            viewModel.putFilterOptions()
            val bundle = Bundle()
            bundle.putBoolean(IS_FILTER_CHANGED, true)
            setFragmentResult(IS_FILTER_CHANGED, bundle)
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.clearTempFilterOptions()
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.observeAreaState().observe(viewLifecycleOwner) {
            renderButtonWithSelectedValues(
                it, binding.areaCustomView, resources.getString(R.string.workplace)
            )
        }

        viewModel.observeIndustryState().observe(viewLifecycleOwner) {
            renderButtonWithSelectedValues(
                it, binding.industryCustomView, resources.getString(R.string.industry)
            )
        }

        viewModel.observeSalaryState().observe(viewLifecycleOwner) {
            if (it != salary) {
                binding.selectedEnterTheAmountTextInputEditText.setText(it)
            }
            viewModel.updateButtonsStates()
        }

        viewModel.observeOnlyWithSalaryState().observe(viewLifecycleOwner) {
            binding.filteringSettingsOnlyWithSalaryCheckbox.isChecked = it
            viewModel.updateButtonsStates()
        }
        viewModel.observeClearAreaButtonNavigation().observe(viewLifecycleOwner) {
            renderClearAreaButtonNavigation(it)
        }

        viewModel.observeClearIndustryButtonNavigation().observe(viewLifecycleOwner) {
            renderClearIndustryButtonNavigation(it)
        }

        viewModel.observeApplyButtonState().observe(viewLifecycleOwner) {
            renderButtonState(it, binding.applyButton)
        }

        viewModel.observeResetButtonState().observe(viewLifecycleOwner) {
            renderButtonState(it, binding.resetButton)
        }
    }

    private fun renderClearAreaButtonNavigation(state: ClearFieldButtonNavigationState) {
        viewModel.updateButtonsStates()
        when (state) {
            ClearFieldButtonNavigationState.ClearField -> viewModel.clearArea()
            ClearFieldButtonNavigationState.Navigate -> {
                findNavController().navigate(R.id.action_filteringSettingsFragment_to_filteringChoosingWorkplaceFragment)
            }
        }
    }

    private fun renderClearIndustryButtonNavigation(state: ClearFieldButtonNavigationState) {
        viewModel.updateButtonsStates()
        when (state) {
            ClearFieldButtonNavigationState.ClearField -> viewModel.clearIndustry()
            ClearFieldButtonNavigationState.Navigate -> {
                findNavController().navigate(R.id.action_filteringSettingsFragment_to_filteringSectorFragment)
            }
        }
    }

    private fun initListeners() {
        binding.selectedEnterTheAmountTextInputEditText.doOnTextChanged { input, _, _, _ ->
            binding.enterTheAmountTextInputLayout.apply {
                if (!input.isNullOrBlank()) {
                    salary = input.toString()
                    viewModel.setSalary(input.toString().toInt())
                }
                if (input != null && input.isBlank()) {
                    salary = input.toString()
                    viewModel.clearSalary()
                    viewModel.updateButtonsStates()
                }
            }

            binding.selectedEnterTheAmountTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setTextInputLayoutHintColor(binding.enterTheAmountTextInputLayout, requireContext(), R.color.blue)
                } else {
                    if (salary == "" || salary.isBlank()) {
                        setTextInputLayoutHintColor(
                            binding.enterTheAmountTextInputLayout,
                            requireContext(),
                            R.color.gray_white
                        )
                    } else {
                        setTextInputLayoutHintColor(
                            binding.enterTheAmountTextInputLayout,
                            requireContext(),
                            R.color.black_universal
                        )
                    }
                }
            }

            binding.filteringSettingsOnlyWithSalaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
                binding.selectedEnterTheAmountTextInputEditText.clearFocus()
                viewModel.setOnlyWithSalary(isChecked)
                viewModel.updateButtonsStates()
            }
        }
    }

    private fun renderButtonState(state: ButtonState, button: Button) {
        when (state) {
            ButtonState.Gone -> button.isVisible = false
            ButtonState.Visible -> button.isVisible = true
        }
    }

    private fun renderButtonWithSelectedValues(
        state: FilterFieldsState, customView: ButtonWithSelectedValues, hint: String
    ) {
        when (state) {
            is FilterFieldsState.Content -> customView.render(
                ButtonWithSelectedValuesState.Content(
                    text = state.text, hint = hint
                )
            )

            FilterFieldsState.Empty -> customView.render(
                ButtonWithSelectedValuesState.Empty(
                    hint = hint
                )
            )
        }
    }

    private fun setTextInputLayoutHintColor(
        textInputLayout: TextInputLayout,
        context: Context,
        @ColorRes colorIdRes: Int
    ) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }

    companion object {
        const val BLANK_STRING = ""
        private const val IS_FILTER_CHANGED = "Is filter changed"
    }
}