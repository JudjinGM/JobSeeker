package ru.practicum.android.diploma.common.util.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.common.ui.model.VacancyUi
import ru.practicum.android.diploma.databinding.ItemVacancyBinding

class RecycleViewVacancyAdapter(
    private var clickListener: (VacancyUi) -> Unit = {}
) : RecyclerView.Adapter<VacancyViewHolder>() {
    var items = listOf<VacancyUi>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                DiffCallbackVacancy(field, newList)
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VacancyViewHolder(
            ItemVacancyBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            clickListener(items[holder.adapterPosition])
        }
    }
}