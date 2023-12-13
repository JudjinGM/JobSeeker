package ru.practicum.android.diploma.filter.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.filter_models.Industries
import ru.practicum.android.diploma.common.domain.model.filter_models.IndustryFilter
import ru.practicum.android.diploma.common.util.Resource

interface IndustryRepository {
    fun getIndustries(): Flow<Resource<Industries>>
    fun setIndustry(industry: IndustryFilter)
    fun getChosenIndustry(): IndustryFilter?
}