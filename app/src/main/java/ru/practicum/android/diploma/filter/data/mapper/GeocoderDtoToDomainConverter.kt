package ru.practicum.android.diploma.filter.data.mapper

import android.util.Log
import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter
import ru.practicum.android.diploma.filter.data.model.geocoder.Components
import ru.practicum.android.diploma.filter.data.model.geocoder.GeocoderResponse
import ru.practicum.android.diploma.filter.domain.model.MyLocation

class GeocoderDtoToDomainConverter {
    fun mapGeocoderDtoToAreaFiltersByAreaList(
        geocoderResponse: GeocoderResponse, areas: List<AreaFilter>, countries: List<CountryFilter>
    ): MyLocation {
        var areaFilter: AreaFilter? = null
        var countryFilter: CountryFilter? = null

        val provinceList = mutableListOf<String>()
        val areaList = mutableListOf<String>()

        val featureMember = geocoderResponse.response.geoObjectCollection?.featureMember
        var components: List<Components>? = null

        if (!featureMember.isNullOrEmpty())
            components =
                featureMember[0].geoObject?.metaDataProperty?.geocoderMetaData?.address?.components

        components?.forEach {
            if (it.kind == LOCALITY) {
                areaList.add(it.name ?: EMPTY_STRING)
            }
            Log.d("MY_TAG", "locality = ${it.name}")
        }

        components?.forEach {
            if (it.kind == PROVINCE) {
                provinceList.add(it.name ?: EMPTY_STRING)
            }
            Log.d("MY_TAG", "province = ${it.name}")

        }

        val countryName =
            components?.find {
                it.kind == "country"
            }?.name ?: EMPTY_STRING

        Log.d("MY_TAG", countryName)

        areaList.forEach { areaName ->
            if (areaName.isNotBlank()) {
                areaFilter = areas.find { area ->
                    area.name.contains(areaName, true) && area.countryName.contains(
                        countryName, true
                    )
                }
            }
        }

        provinceList.forEach { provinceName ->
            if (areaFilter == null && provinceName.isNotBlank()) {
                areaFilter = areas.find { province ->
                    province.name.contains(provinceName, true) && province.countryName.contains(
                        countryName, true
                    )
                }
            }
        }
        if (countryName.isNotBlank()) {
            countryFilter = countries.find {
                it.name.contains(countryName, true)
            }
        }
        Log.d("MY_TAG", "area = ${areaFilter?.name}, country = ${countryFilter?.name} ")

        return MyLocation(areaFilter, countryFilter)
    }

    companion object {
        const val LOCALITY = "locality"
        const val PROVINCE = "province"
        const val EMPTY_STRING = ""
    }
}