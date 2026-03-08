package com.example.contactapp.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.contactapp.data.ContactDatabase
import com.example.contactapp.data.entity.Contact
import com.example.contactapp.databinding.FragmentAddContactBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddContactBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddContactBinding
    private lateinit var db: ContactDatabase
    private var imageUrl: String? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()) { uri -> uri?.
    let { requireContext().contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUrl = it.toString()
            binding.ivContactPhoto.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = ContactDatabase.getInstance(requireContext())
        setupTextWatchers()
        setupClickListeners()
    }

    private fun setupTextWatchers() {
        binding.etUserName.addTextChangedListener { binding.tvPreviewName.text = it.toString() }
        binding.etUserEmail.addTextChangedListener { binding.tvPreviewEmail.text = it.toString() }
        binding.etUserPhone.addTextChangedListener { binding.tvPreviewPhone.text = it.toString() }
    }

    private fun setupClickListeners() {
        binding.ivContactPhoto.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.btnEnterUser.setOnClickListener {
            saveContact()
        }
    }

    private fun saveContact() {
        val userName = binding.etUserName.text.toString()
        val userEmail = binding.etUserEmail.text.toString()
        val userPhone = binding.etUserPhone.text.toString()

        if (userName.isNotEmpty() && userEmail.isNotEmpty() && userPhone.isNotEmpty()) {
            val contact = Contact(imageUrl = imageUrl, name = userName, email = userEmail, phone = userPhone)
            lifecycleScope.launch(Dispatchers.IO) {
                db.contactDao().insert(contact)
                withContext(Dispatchers.Main) {
                    (activity as MainActivity).loadContacts()
                    dismiss()
                }
            }
        }
    }
}