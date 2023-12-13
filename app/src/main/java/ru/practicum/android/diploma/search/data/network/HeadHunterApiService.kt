package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.filter.data.model.dto.AreasDto
import ru.practicum.android.diploma.filter.data.model.dto.CountryDto
import ru.practicum.android.diploma.filter.data.model.dto.IndustriesDto
import ru.practicum.android.diploma.search.data.model.VacanciesSearchResponse
import ru.practicum.android.diploma.vacancy.data.model.VacancySearchResponse

interface HeadHunterApiService {
    @GET("/vacancies")
    suspend fun search(
        @QueryMap options: Map<String, String>
    ): VacanciesSearchResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun searchVacancyById(@Path("vacancy_id") vacancyId: Int): VacancySearchResponse

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun searchSimilarVacancies(
        @Path("vacancy_id") vacancyId: Int, @QueryMap options: Map<String, String>
    ): VacanciesSearchResponse

    @GET("/areas")
    suspend fun getAreas(): List<AreasDto>

    @GET("/areas/countries")
    suspend fun getCountries(): List<CountryDto>

    @GET("/industries")
    suspend fun getIndustries(): List<IndustriesDto>

    @GET("/areas/{area_id}")
    suspend fun getAreasById(
        @Path("area_id") areaId: String
    ): AreasDto
}