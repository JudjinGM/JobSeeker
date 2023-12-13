package ru.practicum.android.diploma.filter.data.dataSourceImpl

import ru.practicum.android.diploma.common.domain.model.filter_models.Filter
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalStorageDataSource
import ru.practicum.android.diploma.filter.data.db.FilterDataBase

class FiltersLocalStorageDataSourceImpl(private val filterDataBase: FilterDataBase) :
    FiltersLocalStorageDataSource {
    override fun getFilterOptions(): Filter? = filterDataBase.getFilterOptions()

    override fun setFilterOptions(filters: Filter) = filterDataBase.putFilterOptions(filters)

    override fun clearFilterOptions() = filterDataBase.clearSavedFilter()
}