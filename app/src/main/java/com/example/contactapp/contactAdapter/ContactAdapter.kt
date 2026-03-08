package com.example.contactapp.contactAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.R
import com.example.contactapp.data.entity.Contact
import com.example.contactapp.databinding.ContactItemBinding

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }

    class ContactViewHolder(val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            if (!contact.imageUrl.isNullOrEmpty()) {
                ivContactPhoto.setImageURI(contact.imageUrl.toUri())
            } else {
                ivContactPhoto.setImageResource(R.drawable.contacts_icon)
            }
            tvContactName.text = contact.name
            tvContactEmail.text = contact.email
            tvContactPhone.text = contact.phone
            btnDelete.setOnClickListener { onDeleteClick(contact) }
        }
    }

    override fun getItemCount() = contacts.size
}