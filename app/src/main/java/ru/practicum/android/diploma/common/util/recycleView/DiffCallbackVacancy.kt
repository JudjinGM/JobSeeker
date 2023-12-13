package ru.practicum.android.diploma.common.util.recycleView

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.common.ui.model.VacancyUi

class DiffCallbackVacancy(
    private val oldList: List<VacancyUi>, private val newList: List<VacancyUi>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old == new
    }
}