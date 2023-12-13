package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemSectorBinding
import ru.practicum.android.diploma.filter.DiffCallbackIndustry
import ru.practicum.android.diploma.filter.ui.model.IndustryUi

class RecycleViewIndustryAdapter(
    private var clickListener: (IndustryUi) -> Unit = {}
) : RecyclerView.Adapter<IndustryViewHolder>() {

    var items = listOf<IndustryUi>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                DiffCallbackIndustry(field, newList)
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return IndustryViewHolder(
            ItemSectorBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int  = items.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            clickListener(items[holder.adapterPosition])
        }
    }
}