package ru.practicum.android.diploma.filter.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenAreaUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenCountryUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetAreaFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetCountryFilterUseCase
import ru.practicum.android.diploma.filter.ui.model.ChoosingWorkplaceState

class FilteringChoosingWorkplaceViewModel(
    private val setAreaFilterUseCase: SetAreaFilterUseCase,
    private val setCountryFilterUseCase: SetCountryFilterUseCase,
    getChosenAreaUseCase: GetChosenAreaUseCase,
    getChosenCountryUseCase: GetChosenCountryUseCase
) : ViewModel() {

    private var countryFilter: CountryFilter? = null
    private var areaFilter: AreaFilter? = null

    private val _state = MutableLiveData<ChoosingWorkplaceState>()
    val state: LiveData<ChoosingWorkplaceState> = _state

    private var isClickAllowed = true

    init {
        countryFilter = getChosenCountryUseCase.execute()
        areaFilter = getChosenAreaUseCase.execute()
    }

    fun initializeChoosingWorkplace() {
        updateCountryAndAreaField(countryFilter, areaFilter)
    }

    fun updateCountryFilter(country: CountryFilter) {
        this.countryFilter = country
        updateCountryAndAreaField(countryFilter, areaFilter)
    }

    fun updateCountryAndAreaFilter(country: CountryFilter, area: AreaFilter) {
        this.countryFilter = country
        this.areaFilter = area
        updateCountryAndAreaField(countryFilter, areaFilter)
    }

    fun countryFieldClicked() {
        if (isClickDebounce()) {
            setState(ChoosingWorkplaceState.Navigate.NavigateToCountry)
        }
    }

    fun areaFieldClicked() {
        if (isClickDebounce()) {
            val countryId = countryFilter?.id?.toString()
            setState(ChoosingWorkplaceState.Navigate.NavigateToArea(countryId))
        }
    }

    fun countryButtonClicked() {
        if (isClickDebounce()) {
            if (countryFilter == null) {
                setState(ChoosingWorkplaceState.Navigate.NavigateToCountry)
            } else {
                updateCountryAndAreaField(null, null)
            }
        }
    }

    fun areaButtonClicked() {
        if (isClickDebounce()) {
            if (areaFilter == null) {
                val countryId = countryFilter?.id?.toString()
                setState(ChoosingWorkplaceState.Navigate.NavigateToArea(countryId))
            } else {
                updateCountryAndAreaField(countryFilter, null)
            }
        }
    }

    fun selectButtonClicked() {
        if (isClickDebounce()) {
            addCountryFilter()
            addAreaFilter()
            setState(ChoosingWorkplaceState.Navigate.NavigateBack)
        }
    }

    fun backButtonClicked() {
        if (isClickDebounce()) {
            setState(ChoosingWorkplaceState.Navigate.NavigateBack)
        }
    }

    private fun updateCountryAndAreaField(countryFilter: CountryFilter?, areaFilter: AreaFilter?) {
        when {
            countryFilter == null && areaFilter == null -> {
                setState(ChoosingWorkplaceState.EmptyCountryEmptyArea)
            }

            countryFilter != null && areaFilter == null -> {
                setState(ChoosingWorkplaceState.ContentCountryEmptyArea(countryFilter.name))
            }

            countryFilter == null && areaFilter != null -> {
                setState(ChoosingWorkplaceState.EmptyCountryContentArea(areaFilter.name))
            }

            countryFilter != null && areaFilter != null -> {
                setState(
                    ChoosingWorkplaceState.ContentCountryContentArea(
                        countryFilter.name, areaFilter.name
                    )
                )
            }
        }
        this.countryFilter = countryFilter
        this.areaFilter = areaFilter
    }

    private fun addAreaFilter() {
        setAreaFilterUseCase.execute(areaFilter)
    }

    private fun addCountryFilter() {
        countryFilter?.let { setCountryFilterUseCase.execute(it) }
    }

    private fun setState(state: ChoosingWorkplaceState) {
        _state.value = state
    }

    private fun isClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
    }
}