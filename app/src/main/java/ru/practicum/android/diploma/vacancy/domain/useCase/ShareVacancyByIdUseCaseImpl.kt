package ru.practicum.android.diploma.vacancy.domain.useCase

import ru.practicum.android.diploma.vacancy.domain.repository.ExternalNavigator

class ShareVacancyByIdUseCaseImpl(
    private val externalNavigator: ExternalNavigator
) : ShareVacancyByIdUseCase {
    override fun execute(id: Int) {
        externalNavigator.shareVacancyById(id)
    }
}