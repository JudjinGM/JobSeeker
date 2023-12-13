package ru.practicum.android.diploma.filter.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryAndRegionBinding
import ru.practicum.android.diploma.filter.ui.model.AreaCountryUi

class AreaCountryViewHolder(
    private val binding: ItemCountryAndRegionBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AreaCountryUi) {
        binding.countryItemTextView.text = item.name
    }
}