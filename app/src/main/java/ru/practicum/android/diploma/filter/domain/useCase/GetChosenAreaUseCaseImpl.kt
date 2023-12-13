package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.filter.domain.repository.WorkplaceRepository

class GetChosenAreaUseCaseImpl(private val workplaceRepository: WorkplaceRepository) :
    GetChosenAreaUseCase {
    override fun execute(): AreaFilter? {
        return workplaceRepository.getChosenArea()
    }
}