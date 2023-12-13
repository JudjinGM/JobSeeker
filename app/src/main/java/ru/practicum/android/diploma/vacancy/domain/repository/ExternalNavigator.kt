package ru.practicum.android.diploma.vacancy.domain.repository

interface ExternalNavigator {
    fun openMail(mailTo: String)
    fun callPhone(phoneNumber: String)
    fun shareVacancyById(id: Int)

    fun openAppsSettings()
}