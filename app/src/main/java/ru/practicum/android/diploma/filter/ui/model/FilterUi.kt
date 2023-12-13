package ru.practicum.android.diploma.filter.ui.model

data class FilterUi(
    val countryName: String = "",
    val areaName: String = "",
    val industryName: String = "",
    val salary: String = "",
    val onlyWithSalary: Boolean = false
)