package ru.practicum.android.diploma.common.custom_view.model

sealed interface ButtonWithSelectedValuesLocationState {
    object Gone : ButtonWithSelectedValuesLocationState
    object LocationLoading : ButtonWithSelectedValuesLocationState
    object LocationSuccess : ButtonWithSelectedValuesLocationState
    object LocationEmpty : ButtonWithSelectedValuesLocationState

}