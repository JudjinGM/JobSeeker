package ru.practicum.android.diploma.filter.data.repositoryImpl

import ru.practicum.android.diploma.common.domain.model.filter_models.Filter
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalDataSource
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalStorageDataSource
import ru.practicum.android.diploma.filter.domain.repository.FiltersRepository

class FiltersRepositoryImpl(
    private val filtersLocalDataSource: FiltersLocalDataSource,
    private val filtersLocalStorageDataSource: FiltersLocalStorageDataSource,
) : FiltersRepository {

    override fun getFilterOptions(): Filter? {
        return if (filtersLocalDataSource.getFilterOptions() == null) {
            filtersLocalStorageDataSource.getFilterOptions()
        } else {
            filtersLocalDataSource.getFilterOptions()
        }
    }

    override fun setFilterOptionsToStorage(options: Filter) {
        filtersLocalStorageDataSource.setFilterOptions(options)
        filtersLocalDataSource.clearFilterOptions()
    }

    override fun setSalary(salary: Int) =
        filtersLocalDataSource.setSalary(salary)

    override fun setOnlyWithSalary(option: Boolean) =
        filtersLocalDataSource.setOnlyWithSalary(option)

    override fun clearFilterOptions() =
        filtersLocalStorageDataSource.clearFilterOptions()

    override fun clearArea() =
        filtersLocalDataSource.clearArea()

    override fun clearIndustry() =
        filtersLocalDataSource.clearIndustry()

    override fun clearSalary() =
        filtersLocalDataSource.clearSalary()

    override fun clearTempFilterOptions() =
        filtersLocalDataSource.clearTempFilterOptions()

    override fun isTempFilterOptionsEmpty() =
        filtersLocalDataSource.isTempFilterOptionsEmpty()

    override fun isTempFilterOptionsExists() =
        filtersLocalDataSource.isTempFilterOptionsExists()

    override fun setFilterOptionsToCache(filter: Filter?) =
        filtersLocalDataSource.addFilterToCache(filter)
}