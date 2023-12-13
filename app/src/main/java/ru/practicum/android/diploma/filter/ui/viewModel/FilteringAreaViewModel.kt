package ru.practicum.android.diploma.filter.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.filter_models.AreaFilter
import ru.practicum.android.diploma.common.domain.model.filter_models.Areas
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.useCase.GetAreasUseCase
import ru.practicum.android.diploma.filter.ui.mapper.AreaFilterDomainToRegionCountryUiConverter
import ru.practicum.android.diploma.filter.ui.model.AreaCountryUi
import ru.practicum.android.diploma.filter.ui.model.AreasState
import ru.practicum.android.diploma.search.domain.model.ErrorStatusDomain
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi

class FilteringAreaViewModel(
    private val parentId: String?,
    private val getRegionsUseCase: GetAreasUseCase,
    private val areaFilterDomainToRegionCountryUiConverter: AreaFilterDomainToRegionCountryUiConverter,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<AreasState>()
    private val areasListUi = mutableListOf<AreaCountryUi>()
    private val foundAreasList = mutableListOf<AreaFilter>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, _ -> setState(AreasState.Error(ErrorStatusUi.ERROR_OCCURRED)) }

    private val searchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchAreaInAreasListUi(it)
        }

    private var latestSearchText: String? = null
    private var isClickAllowed = true

    fun observeStateLiveData(): LiveData<AreasState> = stateLiveData
    init {
        setState(AreasState.Loading)
        viewModelScope.launch(coroutineExceptionHandler) {
            getRegionsUseCase.execute(parentId).collect { pair ->
                proceedResult(pair.first, pair.second)
            }
        }
    }

    fun searchAreaDebounce(changedText: String) {
        if (changedText == latestSearchText) {
            return
        }
        latestSearchText = changedText
        searchDebounce(changedText)
    }

    fun areaClicked(areaId: Int) {
        if (isClickDebounce()) {
            val area = foundAreasList.find { areaFilter -> areaFilter.id == areaId }
            stateLiveData.value =
                area?.let { AreasState.Navigate.NavigateWithContent(it) }
        }
    }

    fun proceedBack() {
        stateLiveData.value = AreasState.Navigate.NavigateEmpty
    }

    private fun setState(state: AreasState) {
        stateLiveData.value = state
    }

    private fun proceedResult(areas: Areas?, errorStatusDomain: ErrorStatusDomain?) {
        val foundAreaFilterList = areas?.arealList ?: emptyList()
        foundAreasList.addAll(foundAreaFilterList)
        val areasList = foundAreasList.map {
            areaFilterDomainToRegionCountryUiConverter.mapAreaFilterToRegionCountryUi(it)
        }
        areasListUi.addAll(areasList)
        when (errorStatusDomain) {
            ErrorStatusDomain.NO_CONNECTION -> setState(AreasState.Error(ErrorStatusUi.NO_CONNECTION))
            ErrorStatusDomain.ERROR_OCCURRED -> setState(AreasState.Error(ErrorStatusUi.ERROR_OCCURRED))
            null -> {
                if (areasListUi.isEmpty()) {
                    setState(AreasState.Error(ErrorStatusUi.NOTHING_FOUND))
                } else {
                    setState(AreasState.Success.Content(areasListUi))
                }
            }
        }
    }

    private fun searchAreaInAreasListUi(query: String) {
        if (query.isBlank()) {
            setState(AreasState.Success.Content(areasListUi))
        } else {
            val filteredAreas = areasListUi.filter {
                it.name.contains(query, ignoreCase = true)
            }

            if (filteredAreas.isNotEmpty()) {
                setState(AreasState.Success.Content(filteredAreas))
            } else {
                setState(AreasState.Error(ErrorStatusUi.NOTHING_FOUND))
            }
        }
    }

    private fun isClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                CLICK_DEBOUNCE_DELAY_MILLIS
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 500L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}