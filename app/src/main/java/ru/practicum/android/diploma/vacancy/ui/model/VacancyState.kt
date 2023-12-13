package ru.practicum.android.diploma.vacancy.ui.model

import ru.practicum.android.diploma.common.ui.model.VacancyUi

sealed interface VacancyState {
    object Load : VacancyState

    object Error : VacancyState

    data class Content(var isFavorite: Boolean, val vacancy: VacancyUi) : VacancyState

    data class Navigate(val vacancyId: Int) : VacancyState
}