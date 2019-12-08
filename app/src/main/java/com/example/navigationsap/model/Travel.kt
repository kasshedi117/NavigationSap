package com.example.navigationsap.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.navigationsap.AddressConverter

@Entity
@TypeConverters(AddressConverter::class)
data class Travel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "departure")
    val departure: Address,
    @ColumnInfo(name = "arrival")
    val arrival: Address)