package com.example.contacts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

interface SelectionCallback {
    fun onSelectionModeChanged(isEnabled: Boolean)
}

class ContactAdapter(
    private val onClick: (Contact) -> Unit,
    private val onToggleSelection: (Contact) -> Unit,
    private val selectionCallback: SelectionCallback
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DiffCallback()) {

    var isSelectionMode = false
    private var selectedItems: Set<Contact> = emptySet()

    fun onItemMove(fromPosition: Int, toPosition: Int, updateViewModel: (List<Contact>) -> Unit): Boolean {
        val currentList = currentList.toMutableList()
        val movedItem = currentList.removeAt(fromPosition)
        currentList.add(toPosition, movedItem)
        submitList(currentList)
        updateViewModel(currentList)
        return true
    }

    object ViewUtils {
        fun getBackgroundColor(isSelected: Boolean): Int {
            return if (isSelected) Color.LTGRAY else Color.TRANSPARENT
        }
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            contact: Contact,
            isSelected: Boolean,
            onClick: (Contact) -> Unit
        ) {
            itemView.apply {
                findViewById<TextView>(R.id.textViewName).text =
                    itemView.context.getString(R.string.contact_name, contact.firstName, contact.lastName)
                findViewById<TextView>(R.id.textViewPhone).text = contact.phoneNumber
                itemView.setBackgroundColor(ViewUtils.getBackgroundColor(isSelected))

                setOnClickListener { onClick(contact) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(
            contact,
            isSelected = selectedItems.contains(contact),
            onClick = { clickedContact ->
                if (isSelectionMode) {
                    onToggleSelection(clickedContact)
                } else {
                    onClick(clickedContact)
                }
            }
        )
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
    }

    fun updateSelectedItems(selectedItems: Set<Contact>) {
        val oldSelectedItems = this.selectedItems
        this.selectedItems = selectedItems

        val oldList = oldSelectedItems.toList()
        val newList = selectedItems.toList()

        currentList.forEachIndexed { index, contact ->
            val wasSelected = oldList.contains(contact)
            val isSelected = newList.contains(contact)

            if (wasSelected != isSelected) {
                notifyItemChanged(index)
            }
        }
    }
}