package ru.practicum.android.diploma.filter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.custom_view.model.ButtonWithSelectedValuesTextState
import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.databinding.FragmentFilteringChoosingWorkplaceBinding
import ru.practicum.android.diploma.filter.ui.model.ButtonState
import ru.practicum.android.diploma.filter.ui.model.FilterFieldsState
import ru.practicum.android.diploma.filter.ui.viewModel.FilteringChoosingWorkplaceViewModel

class FilteringChoosingWorkplaceFragment : Fragment() {

    private val viewModel by viewModel<FilteringChoosingWorkplaceViewModel>()

    private var _binding: FragmentFilteringChoosingWorkplaceBinding? = null
    private val binding get() = _binding!!

    private var emptyCountryField = true
    private var emptyAreaField = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteringChoosingWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.countryState.observe(viewLifecycleOwner) {
            renderCountryState(it)
        }
        viewModel.regionState.observe(viewLifecycleOwner) {
            renderRegionState(it)
        }
        viewModel.selectButtonState.observe(viewLifecycleOwner) {
            renderSelectButtonState(it)
        }

        setOnClickListeners()
        setOnFragmentResultListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setOnFragmentResultListener() {

        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val country = bundle.get(BUNDLE_KEY_FOR_COUNTRY) as CountryFilter?
            val area = bundle.get(BUNDLE_KEY_FOR_AREA) as AreaFilter?
            if (country != null) {
                viewModel.updateCountryField(country)
                viewModel.updateAreaField(null)
            }
            if (area != null) {
                viewModel.updateAreaField(area)
                viewModel.updateCountryField(
                    CountryFilter(
                        id = area.countryId,
                        name = area.countryName
                    )
                )
            }
        }
    }

    private fun setOnClickListeners() {

        binding.choosingWorkplaceToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.choosingWorkplaceCountryCustomView.setOnClickListener {
            navigateToCountrySelection()
        }

        binding.choosingWorkplaceAreaCustomView.setOnClickListener {
            navigateToAreaSelection()
        }

        binding.choosingWorkplaceSelectButtonTextView.setOnClickListener {
            viewModel.addCountryFilter()
            viewModel.addAreaFilter()
            findNavController().popBackStack()
        }

        binding.choosingWorkplaceCountryCustomView.onIconButtonClick {
            if (emptyCountryField) {
                navigateToCountrySelection()
            } else {
                viewModel.updateCountryField(null)
                viewModel.updateAreaField(null)
                viewModel.updateSelectButton()
            }
        }

        binding.choosingWorkplaceAreaCustomView.onIconButtonClick {
            if (emptyAreaField) {
                navigateToAreaSelection()
            } else {
                viewModel.updateAreaField(null)
                viewModel.updateSelectButton()
            }
        }

    }

    private fun navigateToCountrySelection() {
        val direction =
            FilteringChoosingWorkplaceFragmentDirections.actionFilteringChoosingWorkplaceFragmentToFilteringCountryFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToAreaSelection() {
        val countryFilter = viewModel.countryFilter
        val countryFilterId = countryFilter?.id?.toString()
        val direction =
            FilteringChoosingWorkplaceFragmentDirections.actionFilteringChoosingWorkplaceFragmentToFilteringRegionFragment(
                countryFilterId
            )
        findNavController().navigate(direction)
    }

    private fun renderCountryState(state: FilterFieldsState) {
        when (state) {
            is FilterFieldsState.Empty -> {
                binding.choosingWorkplaceCountryCustomView.renderTextState(
                    ButtonWithSelectedValuesTextState.Empty(
                        getString(
                            R.string.country
                        )
                    )
                )
                emptyCountryField = true
            }

            is FilterFieldsState.Content -> {
                binding.choosingWorkplaceCountryCustomView.renderTextState(
                    ButtonWithSelectedValuesTextState.Content(
                        state.text, getString(
                            R.string.country
                        )
                    )
                )
                emptyCountryField = false
            }
        }
        viewModel.updateSelectButton()
    }

    private fun renderRegionState(state: FilterFieldsState) {
        when (state) {
            is FilterFieldsState.Empty -> {
                binding.choosingWorkplaceAreaCustomView.renderTextState(
                    ButtonWithSelectedValuesTextState.Empty(
                        getString(
                            R.string.region
                        )
                    )
                )
                emptyAreaField = true
            }

            is FilterFieldsState.Content -> {
                binding.choosingWorkplaceAreaCustomView.renderTextState(
                    ButtonWithSelectedValuesTextState.Content(
                        state.text, getString(
                            R.string.region
                        )
                    )
                )
                emptyAreaField = false
            }
        }
        viewModel.updateSelectButton()
    }

    private fun renderSelectButtonState(state: ButtonState) {
        when (state) {
            is ButtonState.Visible -> binding.choosingWorkplaceSelectButtonTextView.visibility =
                View.VISIBLE

            is ButtonState.Gone -> {
                binding.choosingWorkplaceSelectButtonTextView.visibility = View.GONE
            }
        }
    }

    companion object {
        const val REQUEST_KEY = "request key"
        const val BUNDLE_KEY_FOR_COUNTRY = "bundle key for country"
        const val BUNDLE_KEY_FOR_AREA = "bundle key for area"
    }
}