package ru.practicum.android.diploma.filter.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filter.domain.model.MyLocation

interface GeocoderRepository {
    fun getLocation(coordinates: String): Flow<Resource<MyLocation>>
}