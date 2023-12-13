package ru.practicum.android.diploma.filter.domain.model

import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.CountryFilter

class MyLocation(
    val area: AreaFilter?,
    val country: CountryFilter?
)