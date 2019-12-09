package com.example.navigationsap.DAO

import androidx.room.*
import com.example.navigationsap.model.Trip

@Dao
interface TripDao {
    @get:Query("SELECT * FROM trip")
    val all: Trip?

    @Query(
        "SELECT * FROM trip"
    )
    fun getAllTrips(): List<Trip>?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trip: Trip)

}