package com.example.contactapp.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    lateinit var binding: FragmentAddContactBinding
    val MY_REQUEST_GALLERY = 0
    var imageUrl: String? = null
    lateinit var db: ContactDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = ContactDatabase.getInstance(requireContext())

        binding.etUserName.addTextChangedListener { binding.tvPreviewName.text = it.toString() }
        binding.etUserEmail.addTextChangedListener { binding.tvPreviewEmail.text = it.toString() }
        binding.etUserPhone.addTextChangedListener { binding.tvPreviewPhone.text = it.toString() }

            binding.ivContactPhoto.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                startActivityForResult(intent, MY_REQUEST_GALLERY)
            }


        binding.btnEnterUser.setOnClickListener {
            val userName = binding.etUserName.text.toString()
            val userEmail = binding.etUserEmail.text.toString()
            val userPhone = binding.etUserPhone.text.toString()

            if (userName.isNotEmpty() && userEmail.isNotEmpty() && userPhone.isNotEmpty()) {
                val contact = Contact(
                    imageUrl = imageUrl, name = userName, email = userEmail, phone = userPhone
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
            val uri = data.data!!
            requireContext().contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUrl = uri.toString()
            binding.ivContactPhoto.setImageURI(uri)
        }
    }
}