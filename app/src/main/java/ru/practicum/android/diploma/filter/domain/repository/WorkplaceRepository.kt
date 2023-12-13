package ru.practicum.android.diploma.filter.domain.repository

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter

interface WorkplaceRepository {
    fun getChosenArea(): AreaFilter?
    fun getChosenCountry(): CountryFilter?
    fun setArea(area: AreaFilter?)
    fun setCountry(country: CountryFilter)
}