package ru.practicum.android.diploma.filter.domain.useCase

import ru.practicum.android.diploma.filter.domain.repository.FiltersRepository

class ClearTempFilterOptionsUseCaseImpl(
    private val filtersRepository: FiltersRepository
) : ClearTempFilterOptionsUseCase {
    override fun execute() {
        filtersRepository.clearTempFilterOptions()
    }
}