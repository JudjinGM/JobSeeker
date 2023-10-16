package ru.practicum.android.diploma.search.data.model.dto

data class ScheduleDto(
    val id: String, // график работы из справочника schedule: "fullDay"
    val name: String // Название графика работы: "Полный день" | "Удаленная работа"
)