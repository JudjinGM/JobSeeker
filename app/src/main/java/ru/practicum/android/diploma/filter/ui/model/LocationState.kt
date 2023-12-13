package ru.practicum.android.diploma.filter.ui.model

sealed interface LocationState {
    object Loading : LocationState
    object Empty : LocationState
    object Success : LocationState
    object Error : LocationState
}