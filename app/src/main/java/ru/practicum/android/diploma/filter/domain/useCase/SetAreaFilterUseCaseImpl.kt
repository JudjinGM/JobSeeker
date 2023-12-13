package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.filter.domain.repository.WorkplaceRepository

class SetAreaFilterUseCaseImpl(private val workplaceRepository: WorkplaceRepository) :
    SetAreaFilterUseCase {
    override fun execute(area: AreaFilter?) {
        workplaceRepository.setArea(area)
    }
}