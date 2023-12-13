package ru.practicum.android.diploma.filter.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.practicum.android.diploma.filter.domain.useCase.ClearAreaFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.ClearAreaFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.ClearFilterOptionsUseCase
import ru.practicum.android.diploma.filter.domain.useCase.ClearFilterOptionsUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.ClearIndustryFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.ClearIndustryFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.ClearSalaryFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.ClearSalaryFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.ClearTempFilterOptionsUseCase
import ru.practicum.android.diploma.filter.domain.useCase.ClearTempFilterOptionsUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetAreaFromGeocoderUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetAreaFromGeocoderUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetAreasUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetAreasUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenAreaUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenAreaUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenCountryUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenCountryUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenIndustryUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetChosenIndustryUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetCountriesUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetCountriesUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetFilterOptionsUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetFilterOptionsUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.GetIndustriesUseCase
import ru.practicum.android.diploma.filter.domain.useCase.GetIndustriesUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.IsTempFilterOptionsEmptyUseCase
import ru.practicum.android.diploma.filter.domain.useCase.IsTempFilterOptionsEmptyUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.IsTempFilterOptionsExistsUseCase
import ru.practicum.android.diploma.filter.domain.useCase.IsTempFilterOptionsExistsUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetAreaFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetAreaFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetCountryFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetCountryFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetFilterOptionsUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetFilterOptionsUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetFilterToTempUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetFilterToTempUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetIndustryFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetIndustryFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetOnlyWithSalaryFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetOnlyWithSalaryFilterUseCaseImpl
import ru.practicum.android.diploma.filter.domain.useCase.SetSalaryFilterUseCase
import ru.practicum.android.diploma.filter.domain.useCase.SetSalaryFilterUseCaseImpl
import ru.practicum.android.diploma.vacancy.domain.useCase.OpenAppsSettingsUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.OpenAppsSettingsUseCaseImpl

val filterDomainModule = module {
    factoryOf(::SetAreaFilterUseCaseImpl) bind SetAreaFilterUseCase::class
    factoryOf(::SetCountryFilterUseCaseImpl) bind SetCountryFilterUseCase::class
    factoryOf(::SetIndustryFilterUseCaseImpl) bind SetIndustryFilterUseCase::class
    factoryOf(::SetOnlyWithSalaryFilterUseCaseImpl) bind SetOnlyWithSalaryFilterUseCase::class
    factoryOf(::SetSalaryFilterUseCaseImpl) bind SetSalaryFilterUseCase::class
    factoryOf(::ClearFilterOptionsUseCaseImpl) bind ClearFilterOptionsUseCase::class
    factoryOf(::GetAreasUseCaseImpl) bind GetAreasUseCase::class
    factoryOf(::GetCountriesUseCaseImpl) bind GetCountriesUseCase::class
    factoryOf(::GetFilterOptionsUseCaseImpl) bind GetFilterOptionsUseCase::class
    factoryOf(::GetIndustriesUseCaseImpl) bind GetIndustriesUseCase::class
    factoryOf(::SetFilterOptionsUseCaseImpl) bind SetFilterOptionsUseCase::class
    factoryOf(::ClearAreaFilterUseCaseImpl) bind ClearAreaFilterUseCase::class
    factoryOf(::ClearIndustryFilterUseCaseImpl) bind ClearIndustryFilterUseCase::class
    factoryOf(::ClearSalaryFilterUseCaseImpl) bind ClearSalaryFilterUseCase::class
    factoryOf(::ClearTempFilterOptionsUseCaseImpl) bind ClearTempFilterOptionsUseCase::class
    factoryOf(::IsTempFilterOptionsEmptyUseCaseImpl) bind IsTempFilterOptionsEmptyUseCase::class
    factoryOf(::IsTempFilterOptionsExistsUseCaseImpl) bind IsTempFilterOptionsExistsUseCase::class
    factoryOf(::SetFilterToTempUseCaseImpl) bind SetFilterToTempUseCase::class
    factoryOf(::GetChosenCountryUseCaseImpl) bind GetChosenCountryUseCase::class
    factoryOf(::GetChosenIndustryUseCaseImpl) bind GetChosenIndustryUseCase::class
    factoryOf(::GetChosenAreaUseCaseImpl) bind GetChosenAreaUseCase::class
    factoryOf(::GetAreaFromGeocoderUseCaseImpl) bind GetAreaFromGeocoderUseCase::class
    factoryOf(::OpenAppsSettingsUseCaseImpl) bind OpenAppsSettingsUseCase::class
}
