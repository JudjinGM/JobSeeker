package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.filter.domain.repository.IndustryRepository

class GetChosenIndustryUseCaseImpl(private val industryRepository: IndustryRepository) :
    GetChosenIndustryUseCase {
    override fun execute(): IndustryFilter? {
        return industryRepository.getChosenIndustry()
    }
}