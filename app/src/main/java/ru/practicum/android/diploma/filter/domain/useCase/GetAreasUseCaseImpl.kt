package ru.practicum.android.diploma.filter.domain.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.filter_models.Areas
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filter.domain.repository.AreaRepository
import ru.practicum.android.diploma.search.data.model.ErrorRemoteDataSource
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain

class GetAreasUseCaseImpl(private val areaRepository: AreaRepository) :
    GetAreasUseCase {
    override fun execute(areaId: String?): Flow<Pair<Areas?, ErrorStatusDomain?>> {
        return areaRepository.getAreas(areaId).map { result ->
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