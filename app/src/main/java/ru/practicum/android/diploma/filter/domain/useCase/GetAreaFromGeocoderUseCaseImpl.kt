package ru.practicum.android.diploma.filter.domain.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filter.domain.model.MyLocation
import ru.practicum.android.diploma.filter.domain.repository.GeocoderRepository
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain

class GetAreaFromGeocoderUseCaseImpl(private val geocoderRepository: GeocoderRepository) :
    GetAreaFromGeocoderUseCase {
    override fun execute(coordinates: String): Flow<Pair<MyLocation?, ErrorStatusDomain?>> {
        return geocoderRepository.getLocation(coordinates).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    val errorStatusDomain = when (result.errorStatus) {
                        ErrorRemoteDataSource.NO_CONNECTION -> ErrorStatusDomain.NO_CONNECTION
                        ErrorRemoteDataSource.ERROR_OCCURRED -> ErrorStatusDomain.ERROR_OCCURRED
                        else -> null
                    }
                    Pair(null, errorStatusDomain)
                }
            }

        }
    }
}