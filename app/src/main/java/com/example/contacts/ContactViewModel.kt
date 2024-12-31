package com.example.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ContactViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>(emptyList())
    val contacts: LiveData<List<Contact>> get() = _contacts

    private val _selectedItems = MutableLiveData<Set<Contact>>(emptySet())
    val selectedItems: LiveData<Set<Contact>> get() = _selectedItems

    private val _isSelectionMode = MutableLiveData(false)

    private val _isAddButtonVisible = MutableLiveData(true)
    val isAddButtonVisible: LiveData<Boolean> get() = _isAddButtonVisible

    private val _isDeleteIconVisible = MutableLiveData(true)
    val isDeleteIconVisible: LiveData<Boolean> get() = _isDeleteIconVisible

    private val _isLayoutActionsVisible = MutableLiveData(false)
    val isLayoutActionsVisible: LiveData<Boolean> get() = _isLayoutActionsVisible

    fun setLayoutActionsVisibility(isVisible: Boolean) {
        _isLayoutActionsVisible.value = isVisible
    }

    fun setDeleteIconVisibility(isVisible: Boolean) {
        _isDeleteIconVisible.value = isVisible
    }

    fun setAddButtonVisibility(isVisible: Boolean) {
        _isAddButtonVisible.value = isVisible
    }

    fun setSelectionMode(enabled: Boolean) {
        _isSelectionMode.value = enabled
    }

    fun setContacts(newList: List<Contact>) {
        _contacts.value = newList
    }

    fun addContact(contact: Contact) {
        val updatedList = _contacts.value?.toMutableList() ?: mutableListOf()
        updatedList.add(contact)
        _contacts.value = updatedList
    }

    fun updateContact(updatedContact: Contact) {
        val updatedList = _contacts.value?.map {
            if (it.id == updatedContact.id) updatedContact else it
        } ?: emptyList()
        _contacts.value = updatedList
    }

    fun updateContactList(newList: List<Contact>) {
        _contacts.value = newList
    }

    fun toggleSelection(contact: Contact) {
        val currentSelection = _selectedItems.value?.toMutableSet() ?: mutableSetOf()
        if (currentSelection.contains(contact)) {
            currentSelection.remove(contact)
        } else {
            currentSelection.add(contact)
        }
        _selectedItems.value = currentSelection
    }

    fun clearSelection() {
        _selectedItems.value = emptySet()
    }
}