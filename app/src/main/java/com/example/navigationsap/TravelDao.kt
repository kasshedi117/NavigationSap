package com.example.navigationsap


import androidx.room.*
import com.example.navigationsap.model.Travel

@Dao
interface TravelDao {

    @Query(
        "SELECT * FROM travel"
    )
    fun getAllTravels(): List<Travel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(travel: Travel)

}