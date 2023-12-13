package ru.practicum.android.diploma.vacancy.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.vacancy_models.Vacancy
import ru.practicum.android.diploma.common.ui.mapper.VacancyDomainToVacancyUiConverter
import ru.practicum.android.diploma.vacancy.domain.useCase.AddVacancyToFavoritesUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.CallPhoneUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.CheckInFavoritesUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.DeleteVacancyFromFavoritesUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.FindVacancyByIdUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.OpenMailUseCase
import ru.practicum.android.diploma.vacancy.domain.useCase.ShareVacancyByIdUseCase
import ru.practicum.android.diploma.vacancy.ui.model.VacancyState

class VacancyViewModel(
    private val vacancyId: Int,
    private val findVacancyByIdUseCase: FindVacancyByIdUseCase,
    private val addVacancyToFavoritesUseCase: AddVacancyToFavoritesUseCase,
    private val deleteVacancyFromFavoritesUseCase: DeleteVacancyFromFavoritesUseCase,
    private val checkInFavoritesUseCase: CheckInFavoritesUseCase,
    private val vacancyDomainToVacancyUiConverter: VacancyDomainToVacancyUiConverter,
    private val openMailUseCase: OpenMailUseCase,
    private val shareVacancyByIdUseCase: ShareVacancyByIdUseCase,
    private val callPhoneUseCase: CallPhoneUseCase
) : ViewModel() {

    private val _state = MutableLiveData<VacancyState>()
    val state: LiveData<VacancyState> = _state

    private var vacancy: Vacancy? = null
    private var isFavorite: Boolean = false
    private var isFirstLoad = true

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, _ -> setState(VacancyState.Error) }

    fun initializeVacancy() {
        if (isFirstLoad) {
            isFirstLoad = false
            loadVacancy()
        } else {
            loadVacancyFromCache()
        }
    }

    fun openMail(mailTo: String) {
        openMailUseCase.execute(mailTo)
    }

    fun shareVacancy() {
        if (state.value is VacancyState.Content) {
            shareVacancyByIdUseCase.execute(vacancyId)
        }
    }

    fun dialPhone(phoneNumber: String) {
        callPhoneUseCase.execute(phoneNumber)
    }

    fun toggleFavorites() {
        if (state.value is VacancyState.Content) {
            viewModelScope.launch {
                val currentState = (state.value as VacancyState.Content)
                if (currentState.isFavorite) {
                    vacancy?.let { deleteVacancyFromFavoritesUseCase.execute(it) }
                } else {
                    vacancy?.let { addVacancyToFavoritesUseCase.execute(it) }
                }
                setState(
                    VacancyState.Content(!currentState.isFavorite, currentState.vacancy)
                )
            }
        }
    }

    fun similarVacanciesButtonClicked() {
        val currentState = getCurrentState()
        setState(VacancyState.Navigate(vacancyId = vacancyId))
        currentState?.let { setState(it) }
    }

    private fun getCurrentState(): VacancyState? {
        val currentState = when (state.value) {
            is VacancyState.Content -> {
                (state.value as VacancyState.Content).copy()
            }

            VacancyState.Error -> {
                (state.value as VacancyState.Error)
            }

            VacancyState.Load -> (state.value as VacancyState.Load)
            is VacancyState.Navigate -> (state.value as VacancyState.Navigate).copy()
            null -> null
        }
        return currentState
    }

    private fun loadVacancyFromCache() {
        val currentState = getCurrentState()
        if (currentState is VacancyState.Content) {
            viewModelScope.launch(coroutineExceptionHandler) {
                checkInFavoritesUseCase.execute(vacancyId).collect { isInFavorites ->
                    isFavorite = isInFavorites
                    setState(VacancyState.Content(isInFavorites, currentState.vacancy))
                }
            }
        } else {
            loadVacancy()
        }
    }

    private fun loadVacancy() {
        setState(VacancyState.Load)
        viewModelScope.launch(coroutineExceptionHandler) {
            val vacancyUI = findVacancyByIdUseCase.findVacancyById(vacancyId)
            checkInFavoritesUseCase.execute(vacancyId).collect { isInFavorites ->
                isFavorite = isInFavorites

                if (vacancyUI.vacancy != null) {
                    setState(
                        VacancyState.Content(
                            isInFavorites,
                            vacancyDomainToVacancyUiConverter.mapVacancyToVacancyUi(
                                vacancyUI.vacancy
                            )
                        )
                    )
                    vacancy = vacancyUI.vacancy
                } else {
                    setState(VacancyState.Error)
                }
            }
        }
    }

    private fun setState(state: VacancyState) {
        _state.value = state
    }
}