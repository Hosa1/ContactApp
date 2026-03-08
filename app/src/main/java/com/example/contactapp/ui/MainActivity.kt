package com.example.contactapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.contactapp.contactAdapter.ContactAdapter
import com.example.contactapp.data.ContactDatabase
import com.example.contactapp.data.entity.Contact
import com.example.contactapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: ContactDatabase
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDatabase()
        setupRecyclerView()
        setupClickListeners()
        loadContacts()
    }

    private fun setupDatabase() {
        db = ContactDatabase.getInstance(this)
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter(emptyList()) { contact ->
            lifecycleScope.launch(Dispatchers.IO) {
                db.contactDao().deleteContact(contact.id)
                withContext(Dispatchers.Main) { loadContacts() }
            }
        }
        binding.rvContact.adapter = adapter
        binding.fabBtnDelete.visibility = View.GONE
    }

    private fun setupClickListeners() {
        binding.fabBtn.setOnClickListener { showSheetFragment() }
        binding.fabBtnDelete.setOnClickListener { deleteLastContact() }
    }

    fun loadContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = db.contactDao().getAllContacts()
            withContext(Dispatchers.Main) {
                adapter.updateContacts(contacts)
                updateEmptyState(contacts)
            }
        }
    }

    private fun showSheetFragment() {
        AddContactBottomSheetFragment().show(supportFragmentManager, null)
    }

    private fun updateEmptyState(contacts: List<Contact>) {
        val isEmpty = contacts.isEmpty()
        binding.tvThereIsNoContactsAddedHere.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.lottieAnimationView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.fabBtnDelete.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun deleteLastContact() {
        lifecycleScope.launch(Dispatchers.IO) {
            db.contactDao().deleteLastContact()
            withContext(Dispatchers.Main) { loadContacts() }
        }
    }
}