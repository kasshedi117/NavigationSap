package com.example.navigationsap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.navigationsap.DAO.TravelDao
import com.example.navigationsap.DAO.TripDao
import com.example.navigationsap.model.Travel
import com.example.navigationsap.model.Trip

@Database(entities = [Trip::class, Travel::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun travelDao(): TravelDao
    companion object {
        var INSTANCE: AppDataBase? = null

        fun getAppDataBase(context: Context): AppDataBase? {
            if (INSTANCE == null){
                synchronized(AppDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}