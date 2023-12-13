package ru.practicum.android.diploma.filter.data.repositoryImpl

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalDataSource
import ru.practicum.android.diploma.filter.domain.repository.WorkplaceRepository

class WorkplaceRepositoryImpl(private val filtersLocalDataSource: FiltersLocalDataSource) :
    WorkplaceRepository {
    override fun getChosenArea(): AreaFilter? {
        return filtersLocalDataSource.getFilterOptions()?.area
    }

    override fun getChosenCountry(): CountryFilter? {
        return filtersLocalDataSource.getFilterOptions()?.country
    }

    override fun setArea(area: AreaFilter?) {
        filtersLocalDataSource.setArea(area)
    }

    override fun setCountry(country: CountryFilter) {
        filtersLocalDataSource.setCountry(country)
    }
}