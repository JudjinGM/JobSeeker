package ru.practicum.android.diploma.filter.domain.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.filter_models.Countries
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filter.domain.repository.CountryRepository
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain

class GetCountriesUseCaseImpl(private val countryRepository: CountryRepository) :
    GetCountriesUseCase {
    override fun execute(): Flow<Pair<Countries?, ErrorStatusDomain?>> {
        return countryRepository.getCountries().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    val errorStatusDomain = when (result.errorStatus) {
                        ErrorRemoteDataSource.NO_CONNECTION -> ErrorStatusDomain.NO_CONNECTION
                        ErrorRemoteDataSource.ERROR_OCCURRED -> ErrorStatusDomain.ERROR_OCCURRED
                        else -> null
                    }
                    Pair(null, errorStatusDomain)
                }
            }
        }
    }
}