package ru.practicum.android.diploma.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryAndRegionBinding
import ru.practicum.android.diploma.filter.ui.AreaCountryViewHolder
import ru.practicum.android.diploma.filter.ui.model.AreaCountryUi

class RecycleViewAreaAdapter(
    private var clickListener: (AreaCountryUi) -> Unit = {}
) : RecyclerView.Adapter<AreaCountryViewHolder>() {
    var items = listOf<AreaCountryUi>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                DiffCallbackArea(field, newList)
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaCountryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AreaCountryViewHolder(
            ItemCountryAndRegionBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AreaCountryViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            clickListener(items[holder.adapterPosition])
        }
    }
}
