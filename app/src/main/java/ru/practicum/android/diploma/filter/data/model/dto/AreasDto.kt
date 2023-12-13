package ru.practicum.android.diploma.filter.data.model.dto

import com.google.gson.annotations.SerializedName

data class AreasDto(
    val name: String,
    val id: Int,
    @SerializedName("parent_id")
    val parentId: Int?,
    val areas: List<AreasDto>
)