package ru.practicum.android.diploma.common.domain.model.filter_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AreaFilter(
    var id: Int,
    val name: String,
    val countryName: String,
    val countryId: Int
) : Parcelable