package com.example.navigationsap.utils.typeconverter

import android.util.Log
import androidx.room.TypeConverter
import com.example.navigationsap.model.Address
import com.google.gson.Gson


/**
 * Type Converter to instruct Room how to serialize and deserialize List(s) of data
 */
class ListConverter {

    @TypeConverter
    fun listToJson(value: List<Address>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String?): List<Address>? {

        val objects = Gson().fromJson(value, Array<Address>::class.java) as Array<Address>
        val list = objects.toList()
        return list
    }

}
