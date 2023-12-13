package ru.practicum.android.diploma.filter.ui.model

import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi

sealed interface IndustryState {
    object Loading : IndustryState

    sealed class Success(
        open val industryList: List<IndustryUi>,
        open val chosenIndustryPosition: Int,
        open val isIndustryChosen: Boolean
    ) : IndustryState {
        data class Content(
            override val industryList: List<IndustryUi>,
            override val chosenIndustryPosition: Int,
            override val isIndustryChosen: Boolean
        ) : Success(industryList, chosenIndustryPosition, isIndustryChosen)
    }

    data class Error(
        val errorStatus: ErrorStatusUi
    ) : IndustryState

    sealed interface Navigate : IndustryState {
        class NavigateWithContent(val industryFilter: IndustryFilter?) : Navigate
        object NavigateEmpty : Navigate
    }
}