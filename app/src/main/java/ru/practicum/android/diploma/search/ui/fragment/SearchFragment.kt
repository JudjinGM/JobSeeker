package ru.practicum.android.diploma.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.model.VacancyUi
import ru.practicum.android.diploma.common.util.recycleView.RecycleViewVacancyAdapter
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi
import ru.practicum.android.diploma.search.ui.model.SearchState
import ru.practicum.android.diploma.search.ui.model.TextWatcherJustOnTextChanged
import ru.practicum.android.diploma.search.ui.viewModel.SearchViewModel

open class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private var textWatcher: TextWatcher? = null
    protected var vacanciesAdapter: RecycleViewVacancyAdapter? = null
    private var inputMethodManager: InputMethodManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyViews()
    }

    protected open fun initViews() {
        recycleViewInit()
        fragmentResultListenerInit()
        setOnClicksAndActions()
        setOnTextWatchersTextChangeListeners()
        setOnScrollForRecycleView(
            binding.searchScreenRecyclerView, vacanciesAdapter, viewModel
        )

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderSearchState(it)
        }
    }

    protected open fun destroyViews() {
        textWatcher?.let { binding.searchScreenEditText.removeTextChangedListener(it) }
        binding.searchScreenRecyclerView.adapter = null
        vacanciesAdapter = null
        _binding = null
    }

    protected open fun recycleViewInit() {
        vacanciesAdapter = RecycleViewVacancyAdapter { vacancy ->
            viewModel.vacancyClicked(vacancy.id)
        }

        binding.searchScreenRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchScreenRecyclerView.adapter = vacanciesAdapter

        binding.searchScreenRecyclerView.itemAnimator = null
    }

    protected fun renderSearchState(state: SearchState) {
        when (state) {
            is SearchState.Loading.LoadingNewSearch -> showLoadingSearch(state.isFilterExist)
            is SearchState.Loading.LoadingPaginationSearch -> showLoadingPages()
            is SearchState.Success.Empty -> showEmpty(state.isFilterExist)
            is SearchState.Success.SearchContent -> showContent(
                state.vacancies, state.foundVacancy, state.isFilterExist
            )

            is SearchState.Error.ErrorNewSearch -> showError(state.errorStatus, state.isFilterExist)
            is SearchState.Error.ErrorPaginationSearch -> showPaginationError(state.errorStatus)
            SearchState.Navigate.NavigateToFilterSettings -> navigateToFilterSettings()
            is SearchState.Navigate.NavigateToVacancy -> navigateToVacancy(state.vacancyId)
        }
    }

    protected open fun navigateToVacancy(vacancyId: Int) {
        val direction = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancyId)
        findNavController().navigate(direction)
    }

    private fun navigateToFilterSettings() {
        val direction = SearchFragmentDirections.actionSearchFragmentToFilteringSettingsFragment()
        findNavController().navigate(direction)
    }

    protected open fun showErrorToast(message: String) {
        showToast(message)
        binding.searchScreenPaginationProgressBar.isVisible = false
    }

    protected open fun showContent(
        vacancies: List<VacancyUi>, foundVacancies: Int, isFilterExist: Boolean
    ) {
        emptyScreen()
        vacanciesAdapter?.items = vacancies
        binding.counterVacanciesTextView.text = resources.getQuantityString(
            R.plurals.vacancy_plural, foundVacancies, foundVacancies
        )
        binding.counterVacanciesTextView.isVisible = true
        filterButtonBehavior(isFilterExist)
    }

    protected open fun showEmpty(isFilterExist: Boolean) {
        emptyScreen()
        vacanciesAdapter?.items = listOf()
        binding.placeholderSearchVacanciesImageView.isVisible = true
        filterButtonBehavior(isFilterExist)
    }

    protected open fun showLoadingSearch(isFilterExist: Boolean) {
        emptyScreen()
        vacanciesAdapter?.items = listOf()
        binding.searchScreenFirstLoadingProgressBar.isVisible = true
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchScreenEditText.windowToken, 0
        )
        filterButtonBehavior(isFilterExist)
    }

    protected open fun showLoadingPages() {
        binding.searchScreenPaginationProgressBar.isVisible = true
        binding.searchScreenFirstLoadingProgressBar.isVisible = false
        binding.placeholderSearchVacanciesImageView.isVisible = false
        binding.searchScreenNoInternetPlaceholder.isVisible = false
        binding.searchScreenNothingFoundPlaceholder.isVisible = false
        binding.searchScreenServerErrorPlaceholder.isVisible = false
    }

    protected open fun showError(errorStatus: ErrorStatusUi, isFilterExist: Boolean) {
        when (errorStatus) {
            ErrorStatusUi.NO_CONNECTION -> {
                vacanciesAdapter?.items = listOf()
                emptyScreen()
                binding.searchScreenNoInternetPlaceholder.isVisible = true
            }

            ErrorStatusUi.ERROR_OCCURRED -> {
                vacanciesAdapter?.items = listOf()
                emptyScreen()
                binding.searchScreenServerErrorPlaceholder.isVisible = true
            }

            ErrorStatusUi.NOTHING_FOUND -> {
                vacanciesAdapter?.items = listOf()
                emptyScreen()
                binding.searchScreenNothingFoundPlaceholder.isVisible = true
                binding.counterVacanciesTextView.text =
                    resources.getString(R.string.there_are_no_such_vacancies)
                binding.counterVacanciesTextView.isVisible = true
            }
        }
        filterButtonBehavior(isFilterExist)
    }

    protected open fun setOnClicksAndActions() {
        inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchScreenEditText.windowToken, 0
        )

        binding.searchFormButton.setOnClickListener {
            binding.searchScreenEditText.setText(DEFAULT_TEXT)
        }

        binding.searchVacanciesToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.searchScreenToolbarFilterMenu -> {
                    viewModel.filterSettingsButtonClicked()
                }
            }
            true
        }
    }

    protected fun setOnScrollForRecycleView(
        recyclerView: RecyclerView, adapter: RecycleViewVacancyAdapter?, viewModel: SearchViewModel
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter?.itemCount
                    if (itemsCount != null && pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    protected open fun emptyScreen() {
        binding.apply {
            counterVacanciesTextView.isVisible = false
            searchScreenFirstLoadingProgressBar.isVisible = false
            searchScreenPaginationProgressBar.isVisible = false
            placeholderSearchVacanciesImageView.isVisible = false
            searchScreenNoInternetPlaceholder.isVisible = false
            searchScreenNothingFoundPlaceholder.isVisible = false
            searchScreenServerErrorPlaceholder.isVisible = false
        }
        setMenuFilterIcon(R.drawable.ic_filters_unselected)
    }

    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showPaginationError(errorStatus: ErrorStatusUi) {
        when (errorStatus) {
            ErrorStatusUi.NO_CONNECTION -> showErrorToast(resources.getString(R.string.no_internet))
            ErrorStatusUi.ERROR_OCCURRED -> showErrorToast(resources.getString(R.string.failed_to_get_a_list_of_vacancies))
            ErrorStatusUi.NOTHING_FOUND -> {}
        }
    }

    private fun fragmentResultListenerInit() {
        setFragmentResultListener(IS_FILTER_CHANGED) { _, bundle ->
            val isNewFilterSet = bundle.getBoolean(IS_FILTER_CHANGED)
            if (isNewFilterSet) {
                viewModel.filterChanged()
            }
        }
    }

    private fun filterButtonBehavior(isFiltersExist: Boolean) {
        if (isFiltersExist) {
            setMenuFilterIcon(R.drawable.ic_filters_selected)
        } else {
            setMenuFilterIcon(R.drawable.ic_filters_unselected)
        }
    }

    private fun setMenuFilterIcon(drawableInt: Int) {
        binding.searchVacanciesToolbar.menu.findItem(R.id.searchScreenToolbarFilterMenu).icon =
            AppCompatResources.getDrawable(requireContext(), drawableInt)
    }

    private fun setOnTextWatchersTextChangeListeners() {
        textWatcher = object : TextWatcherJustOnTextChanged {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setSearchEditTextEndDrawable(s)

                if (s?.isEmpty() == true) {
                    vacanciesAdapter?.items = listOf()
                    viewModel.clearSearchInput()
                }
                viewModel.searchDebounced(
                    changedText = s?.toString() ?: DEFAULT_TEXT
                )
            }
        }
        textWatcher?.let { binding.searchScreenEditText.addTextChangedListener(it) }
    }

    private fun setSearchEditTextEndDrawable(charSequence: CharSequence?) {
        if (charSequence.isNullOrEmpty()) {
            binding.searchFormButton.setImageResource(R.drawable.ic_search)
        } else {
            binding.searchFormButton.setImageResource(R.drawable.ic_cross)
        }
    }

    companion object {
        const val DEFAULT_TEXT = ""
        private const val IS_FILTER_CHANGED = "Is filter changed"
    }
}