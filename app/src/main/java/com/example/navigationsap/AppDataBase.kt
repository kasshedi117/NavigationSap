package com.example.navigationsap

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.navigationsap.model.Trip

@Database(entities = [Trip::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun addressDao(): AddressDao

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