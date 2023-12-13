package ru.practicum.android.diploma.vacancy.ui.fragment

import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.ui.RecycleViewContactsAdapter
import ru.practicum.android.diploma.vacancy.ui.model.VacancyState
import ru.practicum.android.diploma.vacancy.ui.viewModel.VacancyViewModel


class VacancyFragment : Fragment() {

    private val viewModel: VacancyViewModel by viewModel {
        parametersOf(
            args.vacancyId
        )
    }
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private var phonesAdapter: RecycleViewContactsAdapter? = null
    private val args: VacancyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initializeVacancy()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        setOnClickListeners()
        initializePhonesAdapter()

        binding.vacancySimilarVacanciesButtonTextView.setOnClickListener {
            viewModel.similarVacanciesButtonClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vacancyContactsPhoneRecycleView.adapter = null
        phonesAdapter = null
        _binding = null
    }

    private fun render(state: VacancyState) {
        when (state) {
            is VacancyState.Load -> {
                binding.vacancyProgressBar.visibility = View.VISIBLE
                binding.vacancyServerErrorPlaceholder.visibility = View.GONE
                binding.vacancyContentScrollView.visibility = View.GONE
            }

            is VacancyState.Error -> {
                binding.vacancyProgressBar.visibility = View.GONE
                binding.vacancyServerErrorPlaceholder.visibility = View.VISIBLE
                binding.vacancyContentScrollView.visibility = View.GONE
            }

            is VacancyState.Content -> {
                binding.vacancyProgressBar.visibility = View.GONE
                binding.vacancyServerErrorPlaceholder.visibility = View.GONE
                binding.vacancyContentScrollView.visibility = View.VISIBLE
                setupContent(state)
            }

            is VacancyState.Navigate -> {
                val direction =
                    VacancyFragmentDirections.actionVacancyFragmentToSimilarVacancyFragment(state.vacancyId)
                findNavController().navigate(direction)
            }
        }
    }

    private fun setOnClickListeners() {
        binding.vacancyContactsEmailTextView.setOnClickListener {
            viewModel.openMail(binding.vacancyContactsEmailTextView.text.toString())
        }

        binding.vacancyToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.vacancyToolbar.inflateMenu(R.menu.menu_vacancy_toolbar)
        binding.vacancyToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    viewModel.shareVacancy()
                }

                R.id.like -> {
                    viewModel.toggleFavorites()
                }
            }
            true
        }
    }

    private fun initializePhonesAdapter() {
        phonesAdapter = RecycleViewContactsAdapter { item ->
            viewModel.dialPhone(item.formattedNumber)
        }
        binding.vacancyContactsPhoneRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.vacancyContactsPhoneRecycleView.adapter = phonesAdapter
    }

    private fun setupContent(state: VacancyState.Content) {
        val menu: Menu = binding.vacancyToolbar.menu
        if (state.isFavorite) {
            menu.getItem(1).icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_favorites_on)
        } else {
            menu.getItem(1).icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_favorites_off)
        }

        if (state.vacancy.name.isNotBlank()) {
            binding.vacancyHeaderTextView.text = state.vacancy.name
        } else {
            binding.vacancyHeaderTextView.visibility = View.GONE
        }

        if (state.vacancy.salaryAmount.isNotBlank()) {
            binding.vacancySalaryTextView.text = state.vacancy.salaryAmount
        } else {
            binding.vacancySalaryTextView.visibility = View.GONE
        }

        Glide.with(binding.vacancyLogoImageView).load(state.vacancy.employerLogoUrl90)
            .placeholder(R.drawable.ic_placeholder).centerCrop().transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.corner_radius
                    )
                )
            ).into(binding.vacancyLogoImageView)

        if (state.vacancy.employerName.isNotBlank()) {
            binding.vacancyEmployerNameTextView.text = state.vacancy.employerName
        } else {
            binding.vacancyEmployerNameTextView.visibility = View.GONE
        }

        if (state.vacancy.areaName.isNotBlank()) {
            binding.vacancyAreaTextView.text = state.vacancy.areaName
        } else {
            binding.vacancyAreaTextView.visibility = View.GONE
        }

        if (state.vacancy.experienceName.isNotBlank()) {
            binding.vacancyExperienceTextView.text = state.vacancy.experienceName
        } else {
            binding.vacancyExperienceTextView.visibility = View.GONE
            binding.vacancyExperienceHeaderTextView.visibility = View.GONE
        }

        if (state.vacancy.employmentName.isNotBlank()) {
            binding.vacancyScheduleAndEmploymentTextView.text = state.vacancy.employmentName
        } else {
            binding.vacancyScheduleAndEmploymentTextView.visibility = View.GONE
        }

        if (state.vacancy.employmentName.isBlank() && state.vacancy.experienceName.isBlank())
            binding.vacancyExperienceLinearLayout.visibility = View.GONE

        if (state.vacancy.description.isNotBlank()) {
            binding.vacancyDescriptionTextView.text =
                Html.fromHtml(state.vacancy.description, FROM_HTML_MODE_COMPACT)

        } else {
            binding.vacancyDescriptionLinearLayout.visibility = View.GONE
        }

        if (state.vacancy.keySkills.isNotEmpty()) {
            var textForSkills = ""
            for (skill in state.vacancy.keySkills) {
                textForSkills += "    ·   $skill\n"
            }
            binding.vacancyKeySkillsTextView.text =
                textForSkills.substring(0, textForSkills.length - 1)
        } else {
            binding.vacancyKeySkillsLinearLayout.visibility = View.GONE
        }

        if (state.vacancy.contactsName.isNotBlank()) {
            binding.vacancyContactsNameTextView.text = state.vacancy.contactsName
        } else {
            binding.vacancyContactsNameTextView.visibility = View.GONE
            binding.vacancyContactsNameTitleTextView.visibility = View.GONE
        }

        if (state.vacancy.contactsEmail.isNotBlank()) {
            binding.vacancyContactsEmailTextView.text = state.vacancy.contactsEmail
        } else {
            binding.vacancyContactsEmailTextView.visibility = View.GONE
            binding.vacancyContactsEmailTitleTextView.visibility = View.GONE
        }

        if (state.vacancy.contactsPhones.isNotEmpty()) {
            phonesAdapter?.items = state.vacancy.contactsPhones
        }

        if (state.vacancy.contactsName.isBlank() &&
            state.vacancy.contactsEmail.isBlank() &&
            state.vacancy.contactsPhones.isEmpty()
        ) {
            binding.vacancyContactsLinearLayout.visibility = View.GONE
        }
    }
}