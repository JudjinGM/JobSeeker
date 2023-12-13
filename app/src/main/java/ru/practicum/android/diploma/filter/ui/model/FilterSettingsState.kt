package ru.practicum.android.diploma.filter.ui.model

sealed interface FilterSettingsState {
    data class Content(
        val areaField: String = "",
        val industryField: String = "",
        val salaryField: String = "",
        val onlyWithSalary: Boolean = false,
        val isDataChanged: Boolean = false,
        val isItInitSalaryField: Boolean = true,
        val isItInitOnlySalary: Boolean = true,
    ) : FilterSettingsState

    sealed interface Navigate : FilterSettingsState {
        object NavigateToChoosingWorkplace : Navigate
        object NavigateToChoosingIndustry : Navigate
        object NavigateBackWithoutResult : Navigate
        object NavigateBackWithResult : Navigate
    }
}