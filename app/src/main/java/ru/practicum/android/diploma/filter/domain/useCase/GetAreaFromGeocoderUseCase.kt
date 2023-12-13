package ru.practicum.android.diploma.filter.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.model.MyLocation
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain

interface GetAreaFromGeocoderUseCase {
    fun execute(coordinates: String): Flow<Pair<MyLocation?, ErrorStatusDomain?>>
}