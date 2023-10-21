package ru.practicum.android.diploma.filter.data.dataSourceImpl

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.Filter
import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalDataSource
import ru.practicum.android.diploma.filter.data.db.FilterDataBase
import ru.practicum.android.diploma.filter.data.db.FilterLocalCache

class FiltersLocalDataSourceImpl(
    private val filterDataBase: FilterDataBase,
    private val filterLocalCache: FilterLocalCache
) : FiltersLocalDataSource {
    override fun getFilterOptions(): Filter? {
        return if (filterLocalCache.getFilterCache() == null) {
            filterDataBase.getFilterOptions()
        } else {
            filterLocalCache.getFilterCache()
        }
    }

    override fun putFilterOptions(options: Filter) {
        filterDataBase.putFilterOptions(options)
        filterLocalCache.clearAll()
    }

    override fun addCountry(country: CountryFilter) {
        filterLocalCache.addCountry(country)
    }

    override fun addArea(area: AreaFilter) {
        filterLocalCache.addArea(area)
    }

    override fun addIndustry(industry: IndustryFilter) {
        filterLocalCache.addIndustry(industry)
    }

    override fun addSalary(salary: Int) {
        filterLocalCache.addSalary(salary)
    }

    override fun addOnlyWithSalary(option: Boolean) {
        filterLocalCache.addOnlyWithSalary(option)
    }

    override fun clearFilterOptions() {
        filterDataBase.clearSavedFilter()
        filterLocalCache.clearAll()
    }

    override fun clearArea() {
        filterLocalCache.clearArea()
    }

    override fun clearIndustry() {
        filterLocalCache.clearIndustry()
    }

    override fun clearSalary() {
        filterLocalCache.clearSalary()
    }
}