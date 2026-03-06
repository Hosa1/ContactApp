package com.example.contactapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contactapp.data.entity.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(contact: Contact)

    @Query("SELECT * FROM contacts")
     fun getAllContacts(): List<Contact>

    @Query("DELETE FROM contacts WHERE id = :id")
     fun deleteContact(id: Int)


}
