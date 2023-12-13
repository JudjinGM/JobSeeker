package ru.practicum.android.diploma.filter.data.dataSourceImpl

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.Filter
import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalDataSource
import ru.practicum.android.diploma.filter.data.db.FilterLocalCache

class FiltersLocalDataSourceImpl(
    private val filterLocalCache: FilterLocalCache
) : FiltersLocalDataSource {
    override fun getFilterOptions(): Filter? = filterLocalCache.getFilterCache()

    override fun setCountry(country: CountryFilter) = filterLocalCache.addCountry(country)

    override fun setArea(area: AreaFilter?) = filterLocalCache.addArea(area)

    override fun setIndustry(industry: IndustryFilter) = filterLocalCache.addIndustry(industry)

    override fun setSalary(salary: Int) = filterLocalCache.addSalary(salary)

    override fun setOnlyWithSalary(option: Boolean) = filterLocalCache.addOnlyWithSalary(option)

    override fun clearFilterOptions() = filterLocalCache.clearAll()

    override fun clearArea() = filterLocalCache.clearArea()

    override fun clearIndustry() = filterLocalCache.clearIndustry()

    override fun clearSalary() = filterLocalCache.clearSalary()

    override fun clearTempFilterOptions() = filterLocalCache.clearAll()

    override fun isTempFilterOptionsEmpty(): Boolean {
        val filter = filterLocalCache.getFilterCache()
        return if (filter != null) {
            filter.country == null && filter.area == null && filter.industry == null && filter.salary == null && !filter.onlyWithSalary
        } else {
            true
        }
    }

    override fun addFilterToCache(filter: Filter?) = filterLocalCache.addFilterToCache(filter)

    override fun isTempFilterOptionsExists(): Boolean = filterLocalCache.getFilterCache() != null
}