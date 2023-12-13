package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.filter.domain.repository.WorkplaceRepository

class SetCountryFilterUseCaseImpl(private val workplaceRepository: WorkplaceRepository) :
    SetCountryFilterUseCase {
    override fun execute(country: CountryFilter) {
        workplaceRepository.setCountry(country)
    }
}