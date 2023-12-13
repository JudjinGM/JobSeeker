package ru.practicum.android.diploma.vacancy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.common.ui.model.PhoneUi
import ru.practicum.android.diploma.databinding.ItemPhonesBinding

class RecycleViewContactsAdapter(
    private var clickListener: (PhoneUi) -> Unit = {}
) : RecyclerView.Adapter<ContactsPhoneViewHolder>() {
    var items = listOf<PhoneUi>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                DiffCallbackContact(field, newList)
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsPhoneViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactsPhoneViewHolder(
            ItemPhonesBinding.inflate(layoutInflater, parent, false), clickListener
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ContactsPhoneViewHolder, position: Int) {
        holder.bind(items[position])
    }
}