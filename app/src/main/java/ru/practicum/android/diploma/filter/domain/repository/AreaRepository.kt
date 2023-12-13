package ru.practicum.android.diploma.filter.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.filter_models.Areas
import ru.practicum.android.diploma.common.util.Resource

interface AreaRepository{
    fun getAreas(areaId: String?): Flow<Resource<Areas>>
}