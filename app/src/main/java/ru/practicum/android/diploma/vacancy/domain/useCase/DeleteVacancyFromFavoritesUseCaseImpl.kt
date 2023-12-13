package ru.practicum.android.diploma.vacancy.domain.useCase

import ru.practicum.android.diploma.common.domain.model.vacancy_models.Vacancy
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository

class DeleteVacancyFromFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : DeleteVacancyFromFavoritesUseCase {
    override suspend fun execute(vacancy: Vacancy) {
        favoritesRepository.deleteFromFavorites(vacancy)
    }
}