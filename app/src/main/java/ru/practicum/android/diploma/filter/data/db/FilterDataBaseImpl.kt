package ru.practicum.android.diploma.filter.data.db

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.common.domain.model.filter_models.Filter
import ru.practicum.android.diploma.common.util.constants.FilterConst.FILTER
import ru.practicum.android.diploma.common.util.constants.FilterConst.SHARED_PREFS_EMPTY

class FilterDataBaseImpl(private val sharedPrefs: SharedPreferences) : FilterDataBase {
    override fun getFilterOptions(): Filter? {
        val json = sharedPrefs.getString(FILTER, SHARED_PREFS_EMPTY)
        return if (json == SHARED_PREFS_EMPTY) {
            null
        } else {
            Gson().fromJson(json, Filter::class.java)
        }
    }

    override fun putFilterOptions(filter: Filter) =
        sharedPrefs.edit().putString(FILTER, Gson().toJson(filter)).apply()

    override fun clearSavedFilter() = sharedPrefs.edit().clear().apply()
}