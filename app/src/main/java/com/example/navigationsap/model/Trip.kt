package com.example.navigationsap.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.navigationsap.ListConverter

@Entity
@TypeConverters(ListConverter::class)
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val departure: String,
    val time: String,
    val date: String,
    val listAddresses: List<Address>? = ArrayList()
)