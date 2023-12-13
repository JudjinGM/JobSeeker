package ru.practicum.android.diploma.filter.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.common.util.constants.RepositoryConst
import ru.practicum.android.diploma.filter.data.dataSource.GeocoderRemoteDataSource
import ru.practicum.android.diploma.filter.data.mapper.FiltersDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.mapper.GeocoderDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.model.AllAreasRequest
import ru.practicum.android.diploma.filter.data.model.AreasResponse
import ru.practicum.android.diploma.filter.data.model.CountriesRequest
import ru.practicum.android.diploma.filter.data.model.CountriesResponse
import ru.practicum.android.diploma.filter.data.model.geocoder.GeocoderRequest
import ru.practicum.android.diploma.filter.data.model.geocoder.GeocoderResponse
import ru.practicum.android.diploma.filter.domain.model.MyLocation
import ru.practicum.android.diploma.filter.domain.repository.GeocoderRepository
import ru.practicum.android.diploma.search.data.dataSource.VacancyRemoteDataSource
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource

class GeocoderRepositoryImpl(
    private val geocoderRemoteDataSource: GeocoderRemoteDataSource,
    private val filtersRemoteDataSource: VacancyRemoteDataSource,
    private val filtersDtoToDomainConverter: FiltersDtoToDomainConverter,
    private val geocoderDtoToDomainConverter: GeocoderDtoToDomainConverter

) : GeocoderRepository {
    override fun getLocation(coordinates: String): Flow<Resource<MyLocation>> = flow {
        val responseAreas = filtersRemoteDataSource.doRequest(AllAreasRequest())

        val responseCountries = filtersRemoteDataSource.doRequest(CountriesRequest())

        val responseGeocoder = geocoderRemoteDataSource.getLocation(GeocoderRequest(coordinates))

        if (responseAreas.resultCode == RepositoryConst.NO_CONNECTION || responseCountries.resultCode == RepositoryConst.NO_CONNECTION || responseGeocoder.resultCode == RepositoryConst.NO_CONNECTION) {
            emit(Resource.Error(ErrorRemoteDataSource.NO_CONNECTION))

        } else if (responseAreas.resultCode == RepositoryConst.RESPONSE_SUCCESS && responseCountries.resultCode == RepositoryConst.RESPONSE_SUCCESS && responseGeocoder.resultCode == RepositoryConst.RESPONSE_SUCCESS) {

            val areas: List<AreaFilter> =
                filtersDtoToDomainConverter.convertAreaResponseToListOfAreaWithoutCountries(
                    responseAreas as AreasResponse, responseCountries as CountriesResponse
                )

            val countries: List<CountryFilter> =
                filtersDtoToDomainConverter.convertCountriesResponseToListOfCountries(
                    responseCountries
                )

            val result = geocoderDtoToDomainConverter.mapGeocoderDtoToAreaFiltersByAreaList(
                responseGeocoder as GeocoderResponse, areas, countries = countries
            )
            emit(Resource.Success(result))

        } else emit(Resource.Error(ErrorRemoteDataSource.ERROR_OCCURRED))
    }
}
