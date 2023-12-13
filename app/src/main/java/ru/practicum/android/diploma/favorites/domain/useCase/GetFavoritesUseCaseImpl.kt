package ru.practicum.android.diploma.favorites.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Vacancy
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository

class GetFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : GetFavoritesUseCase {
    override suspend fun execute(): Flow<List<Vacancy>> = favoritesRepository.getFavorites()
}