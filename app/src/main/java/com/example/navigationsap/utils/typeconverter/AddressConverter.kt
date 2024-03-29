package com.example.navigationsap.utils.typeconverter

import android.util.Log
import androidx.room.TypeConverter
import com.example.navigationsap.model.Address
import com.google.gson.Gson

class AddressConverter {

    @TypeConverter
    fun listToJson(value: Address?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String?): Address? {

        val objects = Gson().fromJson(value, Address::class.java) as Address
        return objects
    }

}