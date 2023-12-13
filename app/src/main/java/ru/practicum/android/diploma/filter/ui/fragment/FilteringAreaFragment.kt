package ru.practicum.android.diploma.filter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilteringAreaBinding
import ru.practicum.android.diploma.filter.RecycleViewAreaAdapter
import ru.practicum.android.diploma.filter.ui.model.AreaCountryUi
import ru.practicum.android.diploma.filter.ui.model.AreasState
import ru.practicum.android.diploma.filter.ui.viewModel.FilteringAreaViewModel
import ru.practicum.android.diploma.search.ui.model.ErrorStatusUi

class FilteringAreaFragment : Fragment() {

    private val args: FilteringAreaFragmentArgs by navArgs()
    private var _binding: FragmentFilteringAreaBinding? = null
    private val binding get() = _binding!!
    private var areasAdapter: RecycleViewAreaAdapter? = null

    private val viewModel by viewModel<FilteringAreaViewModel> {
        parametersOf(args.parentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteringAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleViewInit()

        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.filteringRegionEditText.doOnTextChanged { text, _, _, _ ->
            setSearchEditTextEndDrawable(text)
            if (text != null) {
                viewModel.searchAreaDebounce(text.toString().trim())
            }
        }

        binding.filteringRegionToolbar.setNavigationOnClickListener {
            viewModel.proceedBack()
        }

        binding.filteringRegionFormButton.setOnClickListener {
            binding.filteringRegionEditText.setText(DEFAULT_TEXT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.filteringRegionRecyclerView.adapter = null
        areasAdapter = null
        _binding = null
    }

    private fun renderState(state: AreasState) {
        when (state) {
            is AreasState.Error -> showError(state.errorStatus)
            AreasState.Loading -> showLoading()
            is AreasState.Success.Content -> showContent(state.arealList)
            AreasState.Navigate.NavigateEmpty -> findNavController().popBackStack()

            is AreasState.Navigate.NavigateWithContent -> {
                val bundle = Bundle()
                bundle.putParcelable(BUNDLE_KEY_FOR_AREA, state.areaFilter)
                setFragmentResult(REQUEST_KEY, bundle)
                findNavController().popBackStack()
            }
        }
    }

    private fun showContent(arealList: List<AreaCountryUi>) {
        emptyScreen()
        areasAdapter?.items = arealList
        binding.filteringRegionRecyclerView.layoutManager?.scrollToPosition(TOP_POSITION_TO_SCROLL)
    }

    private fun showLoading() {
        emptyScreen()
        areasAdapter?.items = emptyList()
        binding.areasScreenProgressBar.isVisible = true
    }

    private fun showError(errorStatus: ErrorStatusUi) {
        when (errorStatus) {
            ErrorStatusUi.NO_CONNECTION, ErrorStatusUi.ERROR_OCCURRED -> {
                emptyScreen()
                areasAdapter?.items = emptyList()
                binding.areasScreenErrorPlaceholder.isVisible = true
            }

            ErrorStatusUi.NOTHING_FOUND -> {
                emptyScreen()
                areasAdapter?.items = emptyList()
                binding.areasScreenNotFoundPlaceholder.isVisible = true
            }
        }
    }

    private fun emptyScreen() {
        binding.areasScreenProgressBar.isVisible = false
        binding.areasScreenErrorPlaceholder.isVisible = false
        binding.areasScreenNotFoundPlaceholder.isVisible = false
    }

    private fun recycleViewInit() {
        areasAdapter = RecycleViewAreaAdapter { item ->
            viewModel.areaClicked(item.id)
        }

        binding.filteringRegionRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.filteringRegionRecyclerView.adapter = areasAdapter
    }

    private fun setSearchEditTextEndDrawable(charSequence: CharSequence?) {
        if (charSequence.isNullOrEmpty()) {
            binding.filteringRegionFormButton.setImageResource(R.drawable.ic_search)
        } else {
            binding.filteringRegionFormButton.setImageResource(R.drawable.ic_cross)
        }
    }

    companion object {
        private const val TOP_POSITION_TO_SCROLL = 0
        const val REQUEST_KEY = "request key"
        const val BUNDLE_KEY_FOR_AREA = "bundle key for area"
        const val DEFAULT_TEXT = ""
    }
}