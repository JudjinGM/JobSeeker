package ru.practicum.android.diploma.filter.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.domain.model.filter_models.Industries
import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.common.util.constants.RepositoryConst
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalDataSource
import ru.practicum.android.diploma.filter.data.mapper.FiltersDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.model.IndustriesRequest
import ru.practicum.android.diploma.filter.data.model.IndustriesResponse
import ru.practicum.android.diploma.filter.domain.repository.IndustryRepository
import ru.practicum.android.diploma.search.data.dataSource.VacancyRemoteDataSource
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource

class IndustryRepositoryImpl(
    private val vacancyRemoteDataSource: VacancyRemoteDataSource,
    private val filtersLocalDataSource: FiltersLocalDataSource,
    private val filtersDtoToDomainConverter:FiltersDtoToDomainConverter
) : IndustryRepository {
    override fun getIndustries(): Flow<Resource<Industries>> = flow {
        val response = vacancyRemoteDataSource.doRequest(IndustriesRequest())
        when (response.resultCode) {
            RepositoryConst.NO_CONNECTION -> {
                emit(Resource.Error(ErrorRemoteDataSource.NO_CONNECTION))
            }

            RepositoryConst.RESPONSE_SUCCESS -> {
                val industries: List<IndustryFilter> =
                    filtersDtoToDomainConverter.convertIndustryResponseToListOfIndustries(response as IndustriesResponse)
                emit(Resource.Success(Industries(industries)))
            }

            else -> {
                emit(Resource.Error(ErrorRemoteDataSource.ERROR_OCCURRED))
            }
        }
    }

    override fun setIndustry(industry: IndustryFilter) {
        filtersLocalDataSource.setIndustry(industry)
    }

    override fun getChosenIndustry(): IndustryFilter? {
        return filtersLocalDataSource.getFilterOptions()?.industry
    }
}