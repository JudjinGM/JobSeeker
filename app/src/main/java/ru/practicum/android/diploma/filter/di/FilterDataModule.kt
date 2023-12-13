package ru.practicum.android.diploma.filter.di

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.util.constants.FilterConst.JOBSEEKER_SHARED_PREFS
import ru.practicum.android.diploma.common.util.constants.NetworkConst
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalDataSource
import ru.practicum.android.diploma.filter.data.dataSource.FiltersLocalStorageDataSource
import ru.practicum.android.diploma.filter.data.dataSource.GeocoderRemoteDataSource
import ru.practicum.android.diploma.filter.data.dataSourceImpl.FiltersLocalDataSourceImpl
import ru.practicum.android.diploma.filter.data.dataSourceImpl.FiltersLocalStorageDataSourceImpl
import ru.practicum.android.diploma.filter.data.dataSourceImpl.GeocoderRemoteDataSourceImpl
import ru.practicum.android.diploma.filter.data.db.FilterDataBase
import ru.practicum.android.diploma.filter.data.db.FilterDataBaseImpl
import ru.practicum.android.diploma.filter.data.db.FilterLocalCache
import ru.practicum.android.diploma.filter.data.db.FilterLocalCacheImpl
import ru.practicum.android.diploma.filter.data.mapper.FiltersDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.mapper.GeocoderDtoToDomainConverter
import ru.practicum.android.diploma.filter.data.network.YandexGeocoderApiService
import ru.practicum.android.diploma.filter.data.repositoryImpl.FiltersRepositoryImpl
import ru.practicum.android.diploma.filter.domain.repository.AreaRepository
import ru.practicum.android.diploma.filter.data.repositoryImpl.AreaRepositoryImpl
import ru.practicum.android.diploma.filter.domain.repository.WorkplaceRepository
import ru.practicum.android.diploma.filter.data.repositoryImpl.WorkplaceRepositoryImpl
import ru.practicum.android.diploma.filter.domain.repository.CountryRepository
import ru.practicum.android.diploma.filter.data.repositoryImpl.CountryRepositoryImpl
import ru.practicum.android.diploma.filter.data.repositoryImpl.GeocoderRepositoryImpl
import ru.practicum.android.diploma.filter.domain.repository.FiltersRepository
import ru.practicum.android.diploma.filter.domain.repository.IndustryRepository
import ru.practicum.android.diploma.filter.data.repositoryImpl.IndustryRepositoryImpl
import ru.practicum.android.diploma.filter.domain.repository.GeocoderRepository
import ru.practicum.android.diploma.search.domain.mapper.FilterToOptionsConverter

val filterDataModule = module {

    factory<SharedPreferences> {
        androidContext().getSharedPreferences(JOBSEEKER_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    single<YandexGeocoderApiService> {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(NetworkConst.YANDEX_GEOCODER_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexGeocoderApiService::class.java)
    }

    factoryOf(::FilterToOptionsConverter)
    singleOf(::FiltersLocalDataSourceImpl) bind FiltersLocalDataSource::class
    singleOf(::FiltersLocalStorageDataSourceImpl) bind FiltersLocalStorageDataSource::class
    singleOf(::FiltersRepositoryImpl) bind FiltersRepository::class
    singleOf(::AreaRepositoryImpl) bind AreaRepository::class
    singleOf(::WorkplaceRepositoryImpl) bind WorkplaceRepository::class
    singleOf(::CountryRepositoryImpl) bind CountryRepository::class
    singleOf(::IndustryRepositoryImpl) bind IndustryRepository::class
    singleOf(::FilterDataBaseImpl) bind FilterDataBase::class
    singleOf(::FilterLocalCacheImpl) bind FilterLocalCache::class
    factoryOf(::FiltersDtoToDomainConverter)
    factoryOf(::GeocoderDtoToDomainConverter)
    singleOf(::GeocoderRemoteDataSourceImpl) bind GeocoderRemoteDataSource::class
    singleOf(::GeocoderRepositoryImpl) bind GeocoderRepository::class
}