package ru.practicum.android.diploma.filter.data.model.geocoder

import com.google.gson.annotations.SerializedName

data class Locality(
    @SerializedName("LocalityName") val localityName: String?
)
