package ru.practicum.android.diploma.filter.data.model.geocoder

import com.google.gson.annotations.SerializedName

data class Components(
    @SerializedName("kind") val kind: String?,
    @SerializedName("name") val name: String?
)