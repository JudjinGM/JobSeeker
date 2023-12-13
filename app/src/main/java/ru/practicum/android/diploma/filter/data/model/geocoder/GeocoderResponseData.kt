package ru.practicum.android.diploma.filter.data.model.geocoder

import com.google.gson.annotations.SerializedName

data class GeocoderResponseData(
    @SerializedName("GeoObjectCollection") val geoObjectCollection: GeoObjectCollection?
)