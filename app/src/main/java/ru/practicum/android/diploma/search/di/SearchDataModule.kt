package ru.practicum.android.diploma.search.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.util.constants.NetworkConst
import ru.practicum.android.diploma.common.util.constants.NetworkConst.HH_API_BASE_URL
import ru.practicum.android.diploma.search.data.dataSource.VacancyRemoteDataSource
import ru.practicum.android.diploma.search.data.dataSourceImpl.VacancyRemoteDataSourceImpl
import ru.practicum.android.diploma.search.data.mapper.VacancyDtoConverter
import ru.practicum.android.diploma.search.data.network.HeadHunterApiService
import ru.practicum.android.diploma.search.data.network.HeaderInterceptor
import ru.practicum.android.diploma.search.data.repositoryImpl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.repository.SearchRepository

val searchDataModule = module {

    single<HeadHunterApiService> {
        val headerInterceptor = HeaderInterceptor()
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptor.redactHeader(NetworkConst.AUTHORIZATION)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(HH_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterApiService::class.java)
    }

    singleOf(::VacancyRemoteDataSourceImpl) bind VacancyRemoteDataSource::class
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    factoryOf(::VacancyDtoConverter)
}