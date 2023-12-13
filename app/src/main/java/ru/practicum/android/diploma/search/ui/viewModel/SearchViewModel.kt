package ru.practicum.android.diploma.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Vacancies
import ru.practicum.android.diploma.common.ui.mapper.VacancyDomainToVacancyUiConverter
import ru.practicum.android.diploma.common.ui.model.VacancyUi
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.DEFAULT_MAX_PAGES
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.DEFAULT_PAGE
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.DEFAULT_PER_PAGE
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.PAGE_LIMIT
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain
import ru.practicum.android.diploma.search.domain.useCase.IsFiltersExistsUseCase
import ru.practicum.android.diploma.search.domain.useCase.SearchUseCase
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi
import ru.practicum.android.diploma.search.ui.model.SearchState

open class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val vacancyDomainToVacancyUiConverter: VacancyDomainToVacancyUiConverter,
    private val isFiltersExistsUseCase: IsFiltersExistsUseCase
) : ViewModel() {

    private var latestSearchText: String? = null
    private var isClickAllowed = true

    private val stateLiveData =
        MutableLiveData<SearchState>(SearchState.Success.Empty(isFiltersExistsUseCase.execute()))

    private var foundVacancies = DEFAULT_FOUND_VACANCIES
    protected var currentPages = DEFAULT_PAGE
    protected var nextPage = DEFAULT_PAGE
    protected var maxPages = DEFAULT_MAX_PAGES
    protected var perPage = DEFAULT_PER_PAGE
    private var isPaginationAllowed = true

    protected var isNextPageLoading = false
    private val vacanciesList = mutableListOf<VacancyUi>()

    private var job: Job? = null

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchNewRequest(it)
        }

    private val coroutineExceptionHandlerNewRequest =
        CoroutineExceptionHandler { _, _ ->
            setState(
                SearchState.Error.ErrorNewSearch(
                    ErrorStatusUi.ERROR_OCCURRED,
                    isFiltersExistsUseCase.execute()
                )
            )
        }

    private val coroutineExceptionHandlerSameRequest =
        CoroutineExceptionHandler { _, _ ->
            setState(
                SearchState.Error.ErrorPaginationSearch(
                    ErrorStatusUi.ERROR_OCCURRED
                )
            )
        }

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchDebounced(changedText: String) {
        if (changedText == latestSearchText) {
            return
        }
        latestSearchText = changedText
        nextPage = 0
        tracksSearchDebounce(changedText)
    }

    fun filterChanged() {
        when (stateLiveData.value) {
            is SearchState.Success.Empty, is SearchState.Success.SearchContent, is SearchState.Error.ErrorNewSearch -> {
                nextPage = 0
                latestSearchText?.let { searchNewRequest(it) }
            }

            else -> {}
        }
    }

    fun filterSettingsButtonClicked() {
        if (isClickDebounce()) {
            val currentState: SearchState? = getCurrentState()
            setState(SearchState.Navigate.NavigateToFilterSettings)
            currentState?.let { setState(it) }
        }
    }

    fun vacancyClicked(vacancyId: Int) {
        if (isClickDebounce()) {
            val currentState: SearchState? = getCurrentState()
            setState(SearchState.Navigate.NavigateToVacancy(vacancyId))
            currentState?.let { setState(it) }
        }
    }

    open fun onLastItemReached() {
        if (isPaginationDebounce()) {
            latestSearchText?.let { searchSameRequest(it) }
        }
    }

    fun clearSearchInput() {
        setState(SearchState.Success.Empty(isFiltersExistsUseCase.execute()))
        tracksSearchDebounce(DEFAULT_TEXT)
        job?.cancel()
    }

    fun setState(state: SearchState) {
        stateLiveData.value = state
    }

    protected fun processResult(
        vacancies: Vacancies?, errorStatus: ErrorStatusDomain?, isNewSearch: Boolean
    ) {
        updateVacanciesListAndFields(vacancies, isNewSearch)
        when (errorStatus) {
            ErrorStatusDomain.NO_CONNECTION -> {
                if (isNewSearch) {
                    setState(
                        SearchState.Error.ErrorNewSearch(
                            ErrorStatusUi.NO_CONNECTION, isFiltersExistsUseCase.execute()
                        )
                    )
                    latestSearchText = DEFAULT_TEXT
                } else {
                    setState(SearchState.Error.ErrorPaginationSearch(ErrorStatusUi.NO_CONNECTION))
                    setState(
                        SearchState.Success.SearchContent(
                            vacanciesList, foundVacancies, isFiltersExistsUseCase.execute()
                        )
                    )
                }
            }

            ErrorStatusDomain.ERROR_OCCURRED -> {
                if (isNewSearch) {
                    setState(
                        SearchState.Error.ErrorNewSearch(
                            ErrorStatusUi.ERROR_OCCURRED, isFiltersExistsUseCase.execute()
                        )
                    )
                    latestSearchText = DEFAULT_TEXT
                } else {
                    setState(SearchState.Error.ErrorPaginationSearch(ErrorStatusUi.ERROR_OCCURRED))
                    setState(
                        SearchState.Success.SearchContent(
                            vacanciesList, foundVacancies, isFiltersExistsUseCase.execute()
                        )
                    )
                }
            }

            null -> {
                if (vacanciesList.isEmpty()) {
                    setState(
                        SearchState.Error.ErrorNewSearch(
                            ErrorStatusUi.NOTHING_FOUND, isFiltersExistsUseCase.execute()
                        )
                    )
                } else {
                    setState(
                        SearchState.Success.SearchContent(
                            vacanciesList, foundVacancies, isFiltersExistsUseCase.execute()
                        )
                    )
                }
            }
        }
    }

    protected fun isPaginationDebounce(): Boolean {
        val current = isPaginationAllowed
        if (isPaginationAllowed) {
            isPaginationAllowed = false

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isPaginationAllowed = true
            }
        }
        return current
    }

    private fun getCurrentState(): SearchState? {
        val currentState: SearchState? = when (stateLiveData.value) {
            is SearchState.Success.SearchContent -> (stateLiveData.value as SearchState.Success.SearchContent).copy()
            is SearchState.Error.ErrorNewSearch -> (stateLiveData.value as SearchState.Error.ErrorNewSearch).copy()
            is SearchState.Error.ErrorPaginationSearch -> (stateLiveData.value as SearchState.Error.ErrorPaginationSearch).copy()
            is SearchState.Loading.LoadingNewSearch -> (stateLiveData.value as SearchState.Loading.LoadingNewSearch).copy()
            SearchState.Loading.LoadingPaginationSearch -> SearchState.Loading.LoadingPaginationSearch
            SearchState.Navigate.NavigateToFilterSettings -> SearchState.Navigate.NavigateToFilterSettings
            is SearchState.Navigate.NavigateToVacancy -> (stateLiveData.value as SearchState.Navigate.NavigateToVacancy).copy()
            is SearchState.Success.Empty -> (stateLiveData.value as SearchState.Success.Empty).copy()
            null -> null
        }
        return currentState
    }

    private fun searchNewRequest(inputSearchText: String) {
        if (inputSearchText.isBlank()) {
            return
        }
        setState(SearchState.Loading.LoadingNewSearch(isFiltersExistsUseCase.execute()))
        foundVacancies = DEFAULT_FOUND_VACANCIES

        nextPage = DEFAULT_PAGE
        job = viewModelScope.launch(coroutineExceptionHandlerNewRequest) {
            getVacancies(inputSearchText, nextPage, perPage, isNewSearch = true)
        }
        nextPage++
    }


    private fun searchSameRequest(inputSearchText: String) {
        if ((currentPages + 1) >= maxPages || currentPages == PAGE_LIMIT || isNextPageLoading) {
            return
        }
        isNextPageLoading = true
        setState(SearchState.Loading.LoadingPaginationSearch)

        if (PAGE_LIMIT - currentPages <= DEFAULT_PER_PAGE) {
            perPage = PAGE_LIMIT - currentPages
        }

        job = viewModelScope.launch(coroutineExceptionHandlerSameRequest) {
            val resultDeferred = async {
                getVacancies(inputSearchText, nextPage, perPage, isNewSearch = false)
            }
            nextPage++
            resultDeferred.await()
            isNextPageLoading = false
        }
    }

    private fun updateVacanciesListAndFields(vacancies: Vacancies?, isNewSearch: Boolean) {
        if (vacancies != null) {
            val foundVacancyUi = vacancies.vacancyList.map {
                vacancyDomainToVacancyUiConverter.mapVacancyToVacancyUi(it)
            }
            if (isNewSearch) {
                vacanciesList.clear()
            }
            vacanciesList.addAll(foundVacancyUi)
            foundVacancies = vacancies.found
            currentPages = vacancies.page
            maxPages = vacancies.pages
        }
    }

    private suspend fun getVacancies(
        inputSearchText: String, nextPage: Int, perPage: Int, isNewSearch: Boolean
    ) {
        searchUseCase.search(inputSearchText, nextPage, perPage = perPage).collect {
            processResult(it.first, it.second, isNewSearch)
        }
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
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val DEFAULT_TEXT = ""
        const val DEFAULT_FOUND_VACANCIES = 0
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
    }
}