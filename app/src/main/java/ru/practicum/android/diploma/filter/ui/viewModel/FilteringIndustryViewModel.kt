package ru.practicum.android.diploma.filter.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.filter_models.Industries
import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenIndustryUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetIndustriesUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetIndustryFilterUseCase
import ru.practicum.android.diploma.filter.ui.mapper.IndustryFilterDomainToIndustryUiConverter
import ru.practicum.android.diploma.filter.ui.model.IndustryState
import ru.practicum.android.diploma.filter.ui.model.IndustryUi
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi

class FilteringIndustryViewModel(
    private val getIndustriesUseCase: GetIndustriesUseCase,
    private val industryFilterDomainToIndustryUiConverter: IndustryFilterDomainToIndustryUiConverter,
    private val setIndustryFilterUseCase: SetIndustryFilterUseCase,
    getChosenIndustryUseCase: GetChosenIndustryUseCase
) : ViewModel() {

    private val stateLiveData = MutableLiveData<IndustryState>()
    private val industriesListUi = mutableListOf<IndustryUi>()
    private var industry: IndustryFilter? = null
    private val foundIndustriesList = mutableListOf<IndustryFilter>()

    private var isClickAllowed = true

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, _ ->
            setState(IndustryState.Error(ErrorStatusUi.ERROR_OCCURRED))
        }

    private val searchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchIndustry(it)
        }

    private var latestSearchText: String? = null

    init {
        industry = getChosenIndustryUseCase.execute()

        stateLiveData.value = IndustryState.Loading

        viewModelScope.launch(coroutineExceptionHandler) {
            getIndustriesUseCase.execute().collect { pair ->
                proceedResult(pair.first, pair.second)
            }
        }
    }

    fun observeStateLiveData(): LiveData<IndustryState> = stateLiveData

    fun searchIndustryDebounce(changedText: String) {
        if (changedText == latestSearchText) {
            return
        }
        latestSearchText = changedText
        searchDebounce(changedText)
    }

    private fun searchIndustry(query: String) {
        if (query.isBlank()) {
            setState(
                IndustryState.Success.Content(
                    industryList = industriesListUi,
                    chosenIndustryPosition = scrollPosition(),
                    isIndustryChosen = isIndustryChosen()
                )
            )
        } else {
            val filteredIndustries = industriesListUi.filter {
                it.name.contains(query, ignoreCase = true)
            }
            if (filteredIndustries.isNotEmpty()) {
                setState(
                    IndustryState.Success.Content(
                        industryList = filteredIndustries,
                        chosenIndustryPosition = scrollPosition(),
                        isIndustryChosen = isIndustryChosen()
                    )
                )
            } else {
                setState(IndustryState.Error(ErrorStatusUi.NOTHING_FOUND))
            }
        }
    }

    fun industryClicked(industryId: Int) {
        if (isClickDebounce()) {
            val currentList = (stateLiveData.value as? IndustryState.Success.Content)?.industryList
            val list = currentList?.map { industryUi ->
                industryUi.copy(isSelected = industryUi.id == industryId)
            }
            industry = foundIndustriesList.find { it.id == industryId }

            if (list != null) {
                setState(
                    IndustryState.Success.Content(
                        industryList = list,
                        chosenIndustryPosition = scrollPosition(),
                        isIndustryChosen = isIndustryChosen()
                    )
                )
            }
            industriesListUi.forEach {
                it.isSelected = it.id == industryId
            }
        }
    }

    fun chooseButtonClicked() {
        if (isClickDebounce()) {
            if (industry != null) {
                setState(IndustryState.Navigate.NavigateWithContent(industry))
            } else {
                setState(IndustryState.Navigate.NavigateEmpty)
            }
        }
    }

    fun saveIndustry() {
        industry?.let { setIndustryFilterUseCase.execute(it) }
    }

    fun proceedBack() {
        if (isClickDebounce()) {
            stateLiveData.value = IndustryState.Navigate.NavigateEmpty
        }
    }

    private fun proceedResult(industries: Industries?, errorStatusDomain: ErrorStatusDomain?) {
        updateIndustriesLists(industries)
        when (errorStatusDomain) {
            ErrorStatusDomain.NO_CONNECTION -> {
                setState(IndustryState.Error(ErrorStatusUi.NO_CONNECTION))
            }

            ErrorStatusDomain.ERROR_OCCURRED -> {
                setState(IndustryState.Error(ErrorStatusUi.ERROR_OCCURRED))
            }

            null -> {
                if (industriesListUi.isEmpty()) {
                    setState(IndustryState.Error(ErrorStatusUi.NOTHING_FOUND))
                } else {
                    setState(
                        IndustryState.Success.Content(
                            industriesListUi,
                            chosenIndustryPosition = scrollPosition(),
                            isIndustryChosen = isIndustryChosen()
                        )
                    )
                }
            }
        }
    }

    private fun updateIndustriesLists(industries: Industries?) {
        val foundIndustries = industries?.industryList ?: emptyList()
        foundIndustriesList.addAll(foundIndustries)
        val industryList = foundIndustries.map {
            industryFilterDomainToIndustryUiConverter.mapIndustryFilterDomainToIndustryUi(it)
        }
        industriesListUi.addAll(industryList)
        if (industry != null) {
            industriesListUi.forEach {
                it.isSelected = it.id == industry?.id
            }
        }
    }

    private fun scrollPosition() = industry?.id ?: 0

    private fun isIndustryChosen() = industry != null

    private fun setState(state: IndustryState) {
        stateLiveData.value = state
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
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 500L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 200L
    }
}