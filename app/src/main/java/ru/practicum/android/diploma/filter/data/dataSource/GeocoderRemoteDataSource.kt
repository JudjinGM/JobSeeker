package ru.practicum.android.diploma.filter.data.dataSource

import ru.practicum.android.diploma.search.data.network.Response

interface GeocoderRemoteDataSource {
    suspend fun getLocation(dto: Any): Response
}