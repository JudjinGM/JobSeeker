package ru.practicum.android.diploma.filter.data.model.geocoder

import com.google.gson.annotations.SerializedName

data class AdministrativeArea(
    @SerializedName("AdministrativeAreaName") val administrativeAreaName: String?,
    @SerializedName("Locality") val locality: Locality?
)