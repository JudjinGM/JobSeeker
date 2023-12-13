package ru.practicum.android.diploma.search.ui.model

import ru.practicum.android.diploma.common.ui.model.VacancyUi


sealed interface SearchState {

    sealed class Loading : SearchState {
        data class LoadingNewSearch(val isFilterExist: Boolean) : Loading()
        object LoadingPaginationSearch : Loading()
    }

    sealed class Success(
        open val vacancies: List<VacancyUi>,
        open val foundVacancy: Int = 0,
        open val isFilterExist: Boolean
    ) : SearchState {
        data class SearchContent(
            override val vacancies: List<VacancyUi>,
            override val foundVacancy: Int,
            override val isFilterExist: Boolean
        ) : Success(vacancies, foundVacancy, isFilterExist)

        data class Empty(
            override val isFilterExist: Boolean
        ) : Success(emptyList(), isFilterExist = isFilterExist)
    }

    sealed class Error(open val errorStatus: ErrorStatusUi) : SearchState {
        data class ErrorNewSearch(
            override val errorStatus: ErrorStatusUi,
            val isFilterExist: Boolean
        ) : Error(errorStatus)

        data class ErrorPaginationSearch(override val errorStatus: ErrorStatusUi) :
            Error(errorStatus)
    }

    sealed interface Navigate : SearchState {
        data class NavigateToVacancy(val vacancyId: Int) : Navigate
        object NavigateToFilterSettings : Navigate
    }
}