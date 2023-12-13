package ru.practicum.android.diploma.filter.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.domain.model.filter_models.Countries
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.common.util.constants.RepositoryConst
import ru.practicum.android.diploma.filter.data.mapper.FiltersDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.model.CountriesRequest
import ru.practicum.android.diploma.filter.data.model.CountriesResponse
import ru.practicum.android.diploma.filter.domain.repository.CountryRepository
import ru.practicum.android.diploma.search.data.dataSource.VacancyRemoteDataSource
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource

class CountryRepositoryImpl(
    private val vacancyRemoteDataSource: VacancyRemoteDataSource,
    private val filtersDtoToDomainConverter: FiltersDtoToDomainConverter
) : CountryRepository {
    override fun getCountries(): Flow<Resource<Countries>> = flow {
        val response = vacancyRemoteDataSource.doRequest(CountriesRequest())
        when (response.resultCode) {
            RepositoryConst.NO_CONNECTION -> {
                emit(Resource.Error(ErrorRemoteDataSource.NO_CONNECTION))
            }

            RepositoryConst.RESPONSE_SUCCESS -> {
                val countries: List<CountryFilter> =
                    filtersDtoToDomainConverter.convertCountriesResponseToListOfCountries(response as CountriesResponse)
                emit(Resource.Success(Countries(countries)))
            }

            else -> {
                emit(Resource.Error(ErrorRemoteDataSource.ERROR_OCCURRED))
            }
        }
    }
}