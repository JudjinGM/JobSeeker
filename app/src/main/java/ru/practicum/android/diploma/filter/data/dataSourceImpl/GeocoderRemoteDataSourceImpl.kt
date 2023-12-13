package ru.practicum.android.diploma.filter.data.dataSourceImpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.util.constants.RepositoryConst.NO_CONNECTION
import ru.practicum.android.diploma.common.util.constants.RepositoryConst.RESPONSE_SUCCESS
import ru.practicum.android.diploma.filter.data.dataSource.GeocoderRemoteDataSource
import ru.practicum.android.diploma.filter.data.model.geocoder.GeocoderRequest
import ru.practicum.android.diploma.filter.data.network.YandexGeocoderApiService
import ru.practicum.android.diploma.search.data.network.Response
import ru.practicum.android.diploma.search.domain.repository.NetworkConnectionProvider

class GeocoderRemoteDataSourceImpl(
    private val yandexGeocoderApiService: YandexGeocoderApiService,
    private val networkConnectionProvider: NetworkConnectionProvider,
) : GeocoderRemoteDataSource {
    override suspend fun getLocation(dto: Any): Response {
        if (!networkConnectionProvider.isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION }
        }
        if (dto is GeocoderRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val apiKey = "73b09e3f-56ab-4107-a4fd-155403dcbe7a"
                    val format = "json"
                    val kind = "locality"
                    val result = 1
                    val response = yandexGeocoderApiService.searchLocation(
                        apiKey = apiKey,
                        coordinates = dto.coordinates,
                        format = format,
                        kind = kind,
                        result = result
                    )
                    response.apply { resultCode = RESPONSE_SUCCESS }
                } catch (e: Throwable) {
                    Response().apply { resultCode = RESPONSE_ERROR }
                }
            }
        } else {
            return Response().apply {
                resultCode =
                    RESPONSE_BAD_REQUEST
            }
        }
    }

    companion object {
        const val RESPONSE_BAD_REQUEST = 400
        const val RESPONSE_ERROR = 500
    }
}