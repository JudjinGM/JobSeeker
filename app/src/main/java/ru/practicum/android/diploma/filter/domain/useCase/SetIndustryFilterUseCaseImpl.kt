package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.filter.domain.repository.IndustryRepository

class SetIndustryFilterUseCaseImpl(private val industryRepository: IndustryRepository) :
    SetIndustryFilterUseCase {
    override fun execute(industry: IndustryFilter) {
        industryRepository.setIndustry(industry)
    }
}