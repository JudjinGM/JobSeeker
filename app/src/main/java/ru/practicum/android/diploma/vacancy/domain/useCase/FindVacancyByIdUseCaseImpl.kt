package ru.practicum.android.diploma.vacancy.domain.useCase

import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain
import ru.practicum.android.diploma.vacancy.domain.model.VacancyOrError
import ru.practicum.android.diploma.vacancy.domain.repository.VacancyRepository

class FindVacancyByIdUseCaseImpl(
    private val repository: VacancyRepository
) : FindVacancyByIdUseCase {
    override suspend fun findVacancyById(vacancyId: Int): VacancyOrError {
        when (val result = repository.findVacancyById(vacancyId)) {
            is Resource.Success -> {
                return VacancyOrError(
                    result.data,
                    null
                )
            }

            is Resource.Error -> {
                val errorStatusDomain = when (result.errorStatus) {
                    ErrorRemoteDataSource.NO_CONNECTION -> ErrorStatusDomain.NO_CONNECTION
                    ErrorRemoteDataSource.ERROR_OCCURRED -> ErrorStatusDomain.ERROR_OCCURRED
                    else -> null
                }
                return VacancyOrError(null, errorStatusDomain)
            }
        }
    }
}