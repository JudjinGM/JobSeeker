package ru.practicum.android.diploma.filter.data.model.geocoder

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("Components") var components: List<Components>
)