package ru.practicum.android.diploma.filter.data.model.geocoder

import com.google.gson.annotations.SerializedName

data class GeoObject(
    @SerializedName("metaDataProperty") var metaDataProperty: MetaDataProperty?
)
