package ru.practicum.android.diploma.common.domain.model.filter_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryFilter(
    var id: Int,
    var name: String
) : Parcelable
