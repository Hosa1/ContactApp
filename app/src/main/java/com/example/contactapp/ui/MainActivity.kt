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
    lateinit var binding: ActivityMainBinding
    lateinit var db: ContactDatabase
    lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ContactDatabase.getInstance(this)
        adapter = ContactAdapter(emptyList()) { contact ->
            lifecycleScope.launch(Dispatchers.IO) {
                db.contactDao().deleteContact(contact.id)
                withContext(Dispatchers.Main) {
                    loadContacts()
                }
            }
        }
        binding.rvContact.adapter = adapter
        binding.fabBtn.setOnClickListener {
            showSheetFragment()
        }
        loadContacts()

    }

    fun loadContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = db.contactDao().getAllContacts()
            withContext(Dispatchers.Main) {
                adapter.updateContacts(contacts)
                checkIfDataBaseEmptyOrNot(contacts)
            }
        }
    }

    private fun showSheetFragment() {
        val addContactBottomSheetFragment = AddContactBottomSheetFragment()
        addContactBottomSheetFragment.show(supportFragmentManager, null)
    }

    private fun checkIfDataBaseEmptyOrNot(contacts: List<Contact>) {
        if (contacts.isNotEmpty()) {
            binding.tvThereIsNoContactsAddedHere.visibility = View.GONE
            binding.lottieAnimationView.visibility = View.GONE
        } else {
            binding.tvThereIsNoContactsAddedHere.visibility = View.VISIBLE
            binding.lottieAnimationView.visibility = View.VISIBLE
        }
    }
}