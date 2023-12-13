package ru.practicum.android.diploma.filter.ui.model

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi

sealed interface AreasState {
    object Loading : AreasState

    sealed class Success(open val arealList: List<AreaCountryUi>) : AreasState {
        data class Content(
            override val arealList: List<AreaCountryUi>
        ) : Success(arealList)
    }

    data class Error(
        val errorStatus: ErrorStatusUi
    ) : AreasState

    sealed interface Navigate : AreasState {
        class NavigateWithContent(val areaFilter: AreaFilter) : Navigate
        object NavigateEmpty : Navigate
    }
}