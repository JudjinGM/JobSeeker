package ru.practicum.android.diploma.vacancy.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository

class CheckInFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : CheckInFavoritesUseCase {
    override suspend fun execute(id: Int): Flow<Boolean> {
        return favoritesRepository.isVacancyContainsFlow(id)
    }
}