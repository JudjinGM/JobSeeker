package ru.practicum.android.diploma.similar_vacancy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.common.ui.model.VacancyUi
import ru.practicum.android.diploma.common.util.recycleView.RecycleViewVacancyAdapter
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.ui.fragment.SearchFragment
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi
import ru.practicum.android.diploma.similar_vacancy.ui.viewModel.SimilarVacancyViewModel

class SimilarVacancyFragment : SearchFragment() {
    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!
    private val args: SimilarVacancyFragmentArgs by navArgs()
    private val viewModel: SimilarVacancyViewModel by viewModel {
        parametersOf(args.vacancyId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViews() {
        recycleViewInit()
        setOnClicksAndActions()
        setOnScrollForRecycleView(
            binding.similarVacanciesRecyclerView, vacanciesAdapter, viewModel
        )

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderSearchState(it)
        }
    }

    override fun destroyViews() {
        binding.similarVacanciesRecyclerView.adapter = null
        vacanciesAdapter = null
        _binding = null
    }

    override fun recycleViewInit() {
        vacanciesAdapter = RecycleViewVacancyAdapter { vacancy ->
            viewModel.vacancyClicked(vacancyId = vacancy.id)
        }

        binding.similarVacanciesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.similarVacanciesRecyclerView.adapter = vacanciesAdapter
    }

    override fun navigateToVacancy(vacancyId: Int) {
        val direction =
            SimilarVacancyFragmentDirections.actionSimilarVacancyFragmentToVacancyFragment(vacancyId)
        findNavController().navigate(direction)
    }

    override fun showErrorToast(message: String) {
        showToast(message)
        binding.similarVacancyScreenPaginationProgressBar.isVisible = false
    }

    override fun showContent(
        vacancies: List<VacancyUi>,
        foundVacancies: Int,
        isFilterExist: Boolean
    ) {
        emptyScreen()
        vacanciesAdapter?.items = vacancies
    }

    override fun showEmpty(isFilterExist: Boolean) {
        emptyScreen()
        vacanciesAdapter?.items = listOf()
    }

    override fun showLoadingSearch(isFilterExist: Boolean) {
        emptyScreen()
        vacanciesAdapter?.items = listOf()
        binding.similarVacanciesScreenFirstLoadingProgressBar.isVisible = true
    }

    override fun showLoadingPages() {
        binding.similarVacancyScreenPaginationProgressBar.isVisible = true
    }

    override fun showError(errorStatus: ErrorStatusUi, isFilterExist: Boolean) {
        when (errorStatus) {
            ErrorStatusUi.NO_CONNECTION -> {
                emptyScreen()
                vacanciesAdapter?.items = listOf()
                binding.similarVacanciesNoInternetPlaceholder.isVisible = true
            }

            ErrorStatusUi.ERROR_OCCURRED -> {
                emptyScreen()
                vacanciesAdapter?.items = listOf()
                binding.similarVacanciesServerErrorPlaceholder.isVisible = true
            }

            ErrorStatusUi.NOTHING_FOUND -> {
                emptyScreen()
                vacanciesAdapter?.items = listOf()
                binding.similarVacanciesNothingFoundPlaceholder.isVisible = true
            }
        }
    }

    override fun setOnClicksAndActions() {
        binding.similarVacanciesToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun emptyScreen() {
        binding.similarVacanciesServerErrorPlaceholder.isVisible = false
        binding.similarVacanciesNoInternetPlaceholder.isVisible = false
        binding.similarVacanciesScreenFirstLoadingProgressBar.isVisible = false
        binding.similarVacancyScreenPaginationProgressBar.isVisible = false
        binding.similarVacanciesNothingFoundPlaceholder.isVisible = false
    }
}