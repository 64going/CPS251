package com.example.test

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "contact_table")
data class Contact(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String,
)