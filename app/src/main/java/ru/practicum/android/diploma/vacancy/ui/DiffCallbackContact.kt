package ru.practicum.android.diploma.vacancy.ui

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.common.ui.model.PhoneUi

class DiffCallbackContact(
    private val oldList: List<PhoneUi>, private val newList: List<PhoneUi>
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