package ru.practicum.android.diploma.filter.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.filter_models.Countries
import ru.practicum.android.diploma.common.util.Resource

interface CountryRepository {
    fun getCountries(): Flow<Resource<Countries>>
}