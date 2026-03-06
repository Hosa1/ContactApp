package com.example.contactapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactapp.data.entity.Contact

@Database(
    entities = [Contact::class], version = 1, exportSchema = false
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        private const val DATA_BASE_NAME = "ContactDatabase"
        private var INSTANCE: ContactDatabase? = null
        fun getInstance(context: Context): ContactDatabase {
            if (INSTANCE == null)
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    DATA_BASE_NAME
                ).allowMainThreadQueries().build()
            return INSTANCE!!
        }
    }


}