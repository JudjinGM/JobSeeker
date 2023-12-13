package ru.practicum.android.diploma.filter.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.Areas
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.common.util.constants.RepositoryConst
import ru.practicum.android.diploma.filter.data.mapper.FiltersDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.model.AllAreasRequest
import ru.practicum.android.diploma.filter.data.model.AreasResponse
import ru.practicum.android.diploma.filter.data.model.CertainAreasRequest
import ru.practicum.android.diploma.filter.data.model.CountriesRequest
import ru.practicum.android.diploma.filter.data.model.CountriesResponse
import ru.practicum.android.diploma.filter.domain.repository.AreaRepository
import ru.practicum.android.diploma.search.data.dataSource.VacancyRemoteDataSource
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource

class AreaRepositoryImpl(
    private val vacancyRemoteDataSource: VacancyRemoteDataSource,
    private val filtersDtoToDomainConverter:FiltersDtoToDomainConverter
) : AreaRepository {
    override fun getAreas(areaId: String?): Flow<Resource<Areas>> = flow {

        val responseAreas = if (areaId == null) {
            vacancyRemoteDataSource.doRequest(AllAreasRequest())
        } else {
            vacancyRemoteDataSource.doRequest(CertainAreasRequest(areaId))
        }

        val responseCountries = vacancyRemoteDataSource.doRequest(CountriesRequest())

        if (responseAreas.resultCode == RepositoryConst.NO_CONNECTION || responseCountries.resultCode == RepositoryConst.NO_CONNECTION) {
            emit(Resource.Error(ErrorRemoteDataSource.NO_CONNECTION))

        } else if (responseAreas.resultCode == RepositoryConst.RESPONSE_SUCCESS && responseCountries.resultCode == RepositoryConst.RESPONSE_SUCCESS) {

            val areas: List<AreaFilter> =
                filtersDtoToDomainConverter.convertAreaResponseToListOfAreaWithoutCountries(
                    responseAreas as AreasResponse, responseCountries as CountriesResponse
                )
            emit(Resource.Success(Areas(areas)))

        } else {
            emit(Resource.Error(ErrorRemoteDataSource.ERROR_OCCURRED))
        }
    }
}