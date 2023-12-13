package ru.practicum.android.diploma.common.domain.model.vacancy_models

data class Employer(
    val id: String?, // Идентификатор компании
    val logoUrls: LogoUrls?, // cсылки на логотипы работодателя разных размеров
    val name: String, // Название компании "HeadHunter"
)