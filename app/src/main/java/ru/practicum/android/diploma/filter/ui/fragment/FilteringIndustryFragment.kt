package ru.practicum.android.diploma.filter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilteringIndustryBinding
import ru.practicum.android.diploma.filter.ui.RecycleViewIndustryAdapter
import ru.practicum.android.diploma.filter.ui.model.IndustryState
import ru.practicum.android.diploma.filter.ui.model.IndustryUi
import ru.practicum.android.diploma.filter.ui.viewModel.FilteringIndustryViewModel
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi

class FilteringIndustryFragment : Fragment() {

    private var _binding: FragmentFilteringIndustryBinding? = null
    private val binding get() = _binding!!
    private var industriesAdapter: RecycleViewIndustryAdapter? = null
    private val viewModel by viewModel<FilteringIndustryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteringIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleViewInit()
        initClicks()

        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.filteringSectorEditText.doOnTextChanged { text, _, _, _ ->
            setSearchEditTextEndDrawable(text)
            if (text != null) {
                viewModel.searchIndustryDebounce(text.toString().trim())
            }
        }

        binding.filteringSectorFormButton.setOnClickListener {
            binding.filteringSectorEditText.setText(DEFAULT_TEXT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.filteringSectorRecyclerView.adapter = null
        industriesAdapter = null
        _binding = null
    }

    private fun recycleViewInit() {
        industriesAdapter = RecycleViewIndustryAdapter { item ->
            viewModel.industryClicked(item.id)
        }

        binding.filteringSectorRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.filteringSectorRecyclerView.adapter = industriesAdapter
    }

    private fun initClicks() {
        binding.filteringIndustriesButton.setOnClickListener {
            viewModel.chooseButtonClicked()
        }

        binding.filteringSectorToolbar.setNavigationOnClickListener {
            viewModel.proceedBack()
        }
    }

    private fun renderState(state: IndustryState) {
        when (state) {
            is IndustryState.Loading -> {
                showLoading()
            }

            is IndustryState.Success -> {
                showContent(
                    state.industryList,
                    state.chosenIndustryPosition,
                    state.isIndustryChosen
                )
            }

            is IndustryState.Error -> showError(state.errorStatus)

            IndustryState.Navigate.NavigateEmpty -> {
                findNavController().popBackStack()
            }

            is IndustryState.Navigate.NavigateWithContent -> {
                viewModel.saveIndustry()
                findNavController().popBackStack()
            }
        }
    }

    private fun showLoading() {
        emptyScreen()
        industriesAdapter?.items = emptyList()
        binding.industryScreenProgressBar.isVisible = true
    }

    private fun showContent(
        industries: List<IndustryUi>, scrollPosition: Int, isIndustryChosen: Boolean
    ) {
        emptyScreen()
        industriesAdapter?.items = industries
        binding.filteringSectorRecyclerView.layoutManager?.scrollToPosition(scrollPosition)
        binding.filteringIndustriesButton.isVisible = isIndustryChosen
    }

    private fun showError(errorStatus: ErrorStatusUi) {
        when (errorStatus) {
            ErrorStatusUi.NO_CONNECTION, ErrorStatusUi.ERROR_OCCURRED -> {
                emptyScreen()
                industriesAdapter?.items = emptyList()
                binding.industriesScreenErrorPlaceholder.isVisible = true
            }

            ErrorStatusUi.NOTHING_FOUND -> {
                emptyScreen()
                industriesAdapter?.items = emptyList()
                binding.industriesScreenNotFoundPlaceholder.isVisible = true
            }
        }
    }

    private fun emptyScreen() {
        binding.industryScreenProgressBar.isVisible = false
        binding.industriesScreenNotFoundPlaceholder.isVisible = false
        binding.industriesScreenErrorPlaceholder.isVisible = false
        binding.filteringIndustriesButton.isVisible = false
    }

    private fun setSearchEditTextEndDrawable(charSequence: CharSequence?) {
        if (charSequence.isNullOrEmpty()) {
            binding.filteringSectorFormButton.setImageResource(R.drawable.ic_search)
        } else {
            binding.filteringSectorFormButton.setImageResource(R.drawable.ic_cross)
        }
    }

    companion object {
        const val DEFAULT_TEXT = ""
    }
}