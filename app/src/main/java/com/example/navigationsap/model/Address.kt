package com.example.navigationsap.model

import androidx.room.Entity

@Entity
data class Address (
    val arrival: String,
    val time: String,
    val date: String)

