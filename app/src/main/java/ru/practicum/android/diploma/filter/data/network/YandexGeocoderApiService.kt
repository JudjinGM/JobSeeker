package ru.practicum.android.diploma.filter.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.filter.data.model.geocoder.GeocoderResponse

interface YandexGeocoderApiService {
    @GET("/1.x")
    suspend fun searchLocation(
        @Query("apikey") apiKey: String,
        @Query("geocode") coordinates: String,
        @Query("format") format: String,
        @Query("kind") kind: String,
        @Query("result") result: Int
    ): GeocoderResponse
}