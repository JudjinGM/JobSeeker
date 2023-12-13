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
import ru.practicum.android.diploma.filter.ui.model.ChoosingWorkplaceState
import ru.practicum.android.diploma.filter.ui.viewModel.FilteringChoosingWorkplaceViewModel

class FilteringChoosingWorkplaceFragment : Fragment() {

    private val viewModel by viewModel<FilteringChoosingWorkplaceViewModel>()
    private var _binding: FragmentFilteringChoosingWorkplaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteringChoosingWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initializeChoosingWorkplace()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
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
            val country = bundle.getParcelable(BUNDLE_KEY_FOR_COUNTRY) as CountryFilter?
            val area = bundle.getParcelable(BUNDLE_KEY_FOR_AREA) as AreaFilter?

            if (country != null) {
                viewModel.updateCountryFilter(country)
            }
            if (area != null) {
                viewModel.updateCountryAndAreaFilter(
                    CountryFilter(
                        id = area.countryId,
                        name = area.countryName
                    ), area
                )
            }
        }
    }

    private fun setOnClickListeners() {
        binding.choosingWorkplaceToolbar.setNavigationOnClickListener {
            viewModel.backButtonClicked()
        }

        binding.choosingWorkplaceCountryCustomView.setOnClickListener {
            viewModel.countryFieldClicked()
        }

        binding.choosingWorkplaceAreaCustomView.setOnClickListener {
            viewModel.areaFieldClicked()
        }

        binding.choosingWorkplaceSelectButtonTextView.setOnClickListener {
            viewModel.selectButtonClicked()
        }

        binding.choosingWorkplaceCountryCustomView.onIconButtonClick {
            viewModel.countryButtonClicked()
        }

        binding.choosingWorkplaceAreaCustomView.onIconButtonClick {
            viewModel.areaButtonClicked()
        }
    }

    private fun render(state: ChoosingWorkplaceState) {
        when (state) {
            is ChoosingWorkplaceState.EmptyCountryEmptyArea -> {
                renderCountryEmpty()
                renderAreaEmpty()
                binding.choosingWorkplaceSelectButtonTextView.visibility = View.GONE
            }

            is ChoosingWorkplaceState.ContentCountryEmptyArea -> {
                renderCountryContent(state.country)
                renderAreaEmpty()
                binding.choosingWorkplaceSelectButtonTextView.visibility = View.VISIBLE
            }

            is ChoosingWorkplaceState.EmptyCountryContentArea -> {
                renderCountryEmpty()
                renderAreaContent(state.area)
                binding.choosingWorkplaceSelectButtonTextView.visibility = View.VISIBLE
            }

            is ChoosingWorkplaceState.ContentCountryContentArea -> {
                renderCountryContent(state.country)
                renderAreaContent(state.area)
                binding.choosingWorkplaceSelectButtonTextView.visibility = View.VISIBLE
            }

            is ChoosingWorkplaceState.Navigate.NavigateToArea -> {
                navigateToAreaSelection(state.countryId)
            }

            is ChoosingWorkplaceState.Navigate.NavigateToCountry -> {
                navigateToCountrySelection()
            }

            is ChoosingWorkplaceState.Navigate.NavigateBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToCountrySelection() {
        val direction =
            FilteringChoosingWorkplaceFragmentDirections.actionFilteringChoosingWorkplaceFragmentToFilteringCountryFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToAreaSelection(countryId: String?) {
        val direction =
            FilteringChoosingWorkplaceFragmentDirections.actionFilteringChoosingWorkplaceFragmentToFilteringRegionFragment(
                countryId
            )
        findNavController().navigate(direction)
    }

    private fun renderCountryEmpty() {
        binding.choosingWorkplaceCountryCustomView.renderTextState(
            ButtonWithSelectedValuesTextState.Empty(
                getString(R.string.country)
            )
        )
    }

    private fun renderCountryContent(country: String) {
        binding.choosingWorkplaceCountryCustomView.renderTextState(
            ButtonWithSelectedValuesTextState.Content(
                country, getString(R.string.country)
            )
        )
    }

    private fun renderAreaEmpty() {
        binding.choosingWorkplaceAreaCustomView.renderTextState(
            ButtonWithSelectedValuesTextState.Empty(
                getString(R.string.region)
            )
        )
    }

    private fun renderAreaContent(area: String) {
        binding.choosingWorkplaceAreaCustomView.renderTextState(
            ButtonWithSelectedValuesTextState.Content(
                area, getString(R.string.region)
            )
        )
    }

    companion object {
        const val REQUEST_KEY = "request key"
        const val BUNDLE_KEY_FOR_COUNTRY = "bundle key for country"
        const val BUNDLE_KEY_FOR_AREA = "bundle key for area"
    }
}