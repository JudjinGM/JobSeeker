package ru.practicum.android.diploma.common.custom_view.model

sealed interface ButtonWithSelectedValuesTextState {
    data class Empty(val hint: String) : ButtonWithSelectedValuesTextState
    data class Content(val text: String, val hint: String) : ButtonWithSelectedValuesTextState
}