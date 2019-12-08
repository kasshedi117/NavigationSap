package com.example.navigationsap.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address (
    val arrival: String,
    val time: String,
    val date: String)