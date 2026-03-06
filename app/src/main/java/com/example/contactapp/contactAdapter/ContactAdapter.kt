package com.example.contactapp.contactAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.R
import com.example.contactapp.data.entity.Contact
import com.example.contactapp.databinding.ContactItemBinding
import java.io.File

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onDeleteClick: (Contact) -> Unit):RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
    class ContactViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var photo = binding.ivContactPhoto
        var name = binding.tvContactName
        var email = binding.tvContactEmail
        var phone = binding.tvContactPhone
        var delete = binding.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(ContactItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        if (!contact.imageUrl.isNullOrEmpty()) {
            holder.photo.setImageURI(Uri.fromFile(File(contact.imageUrl)))
        } else {
            holder.photo.setImageResource(R.drawable.contacts_icon)
        }
        holder.name.text = contact.name
        holder.email.text = contact.email
        holder.phone.text = contact.phone

        holder.delete.setOnClickListener {
            onDeleteClick(contact)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}