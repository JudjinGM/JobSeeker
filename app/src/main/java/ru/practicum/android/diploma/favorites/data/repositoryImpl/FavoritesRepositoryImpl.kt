package ru.practicum.android.diploma.favorites.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Vacancy
import ru.practicum.android.diploma.favorites.data.dataSource.FavoritesLocalDataSource
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository

class FavoritesRepositoryImpl(
    private val favoritesLocalDataSource: FavoritesLocalDataSource,
) : FavoritesRepository {
    override suspend fun addToFavorites(vacancy: Vacancy) =
        favoritesLocalDataSource.addToFavorites(vacancy)

    override suspend fun deleteFromFavorites(vacancy: Vacancy) =
        favoritesLocalDataSource.deleteFromFavorites(vacancy)

    override fun getFavorites(): Flow<List<Vacancy>> =
        favoritesLocalDataSource.getFavorites()

    override suspend fun getVacancy(id: Int): Vacancy =
        favoritesLocalDataSource.getVacancy(id)

    override suspend fun isVacancyContainsFlow(id: Int): Flow<Boolean> =
        favoritesLocalDataSource.isVacancyContainsFlow(id)
}