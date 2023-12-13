package ru.practicum.android.diploma.filter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.filter.ui.model.IndustryUi

class DiffCallbackIndustry(
    private val oldList: List<IndustryUi>, private val newList: List<IndustryUi>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

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