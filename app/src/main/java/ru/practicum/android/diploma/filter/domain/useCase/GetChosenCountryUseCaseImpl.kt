package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.filter.domain.repository.WorkplaceRepository

class GetChosenCountryUseCaseImpl(private val workplaceRepository: WorkplaceRepository) :
    GetChosenCountryUseCase {
    override fun execute(): CountryFilter? {
        return workplaceRepository.getChosenCountry()
    }
}