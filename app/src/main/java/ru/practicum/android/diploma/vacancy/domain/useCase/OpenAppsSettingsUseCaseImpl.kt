package ru.practicum.android.diploma.vacancy.domain.useCase

import ru.practicum.android.diploma.vacancy.domain.repository.ExternalNavigator

class OpenAppsSettingsUseCaseImpl(private val externalNavigator: ExternalNavigator) :
    OpenAppsSettingsUseCase {
    override fun execute() {
        externalNavigator.openAppsSettings()
    }
}