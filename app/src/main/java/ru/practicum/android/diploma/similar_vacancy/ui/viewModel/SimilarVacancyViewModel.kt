package ru.practicum.android.diploma.similar_vacancy.ui.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.mapper.VacancyDomainToVacancyUiConverter
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.DEFAULT_PAGE
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.DEFAULT_PER_PAGE
import ru.practicum.android.diploma.common.util.constants.VacanciesViewModelConst.PAGE_LIMIT
import ru.practicum.android.diploma.search.domain.useCase.IsFiltersExistsUseCase
import ru.practicum.android.diploma.search.domain.useCase.SearchUseCase
import ru.practicum.android.diploma.search.ui.model.SearchState
import ru.practicum.android.diploma.search.ui.viewModel.SearchViewModel
import ru.practicum.android.diploma.similar_vacancy.domain.useCase.SearchSimilarVacanciesByIdUseCase

class SimilarVacancyViewModel(
    val vacancyId: Int,
    private val searchSimilarVacanciesByIdUseCase: SearchSimilarVacanciesByIdUseCase,
    vacancyDomainToVacancyUiConverter: VacancyDomainToVacancyUiConverter,
    searchUseCase: SearchUseCase,
    isFiltersExistsUseCase: IsFiltersExistsUseCase
) : SearchViewModel(
    searchUseCase,
    vacancyDomainToVacancyUiConverter,
    isFiltersExistsUseCase
) {
    init {
        searchSimilarVacancyById(vacancyId)
    }

    override fun onLastItemReached() {
        if (isPaginationDebounce()) {
            searchSameRequest(vacancyId)
        }
    }

    private fun searchSimilarVacancyById(vacancyId: Int) {
        setState(SearchState.Loading.LoadingNewSearch(isFilterExist = false))

        nextPage = DEFAULT_PAGE
        viewModelScope.launch {
            getVacancies(vacancyId, nextPage, perPage, isNewSearch = true)
        }
        nextPage++
    }

    private fun searchSameRequest(vacancyId: Int) {
        if (currentPages == maxPages || currentPages == PAGE_LIMIT || isNextPageLoading) {
            return
        }
        isNextPageLoading = true
        setState(SearchState.Loading.LoadingPaginationSearch)

        if (PAGE_LIMIT - currentPages <= DEFAULT_PER_PAGE) {
            perPage = PAGE_LIMIT - currentPages
        }

        viewModelScope.launch {
            val resultDeferred = async {
                getVacancies(vacancyId, nextPage, perPage, isNewSearch = false)
            }
            nextPage++
            resultDeferred.await()
            isNextPageLoading = false
        }
    }

    private suspend fun getVacancies(
        vacancyId: Int, nextPage: Int, perPage: Int, isNewSearch: Boolean
    ) {
        searchSimilarVacanciesByIdUseCase.execute(vacancyId, nextPage, perPage = perPage).collect {
            processResult(it.first, it.second, isNewSearch)
        }
    }
}