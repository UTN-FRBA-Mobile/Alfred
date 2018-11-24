package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.Dimension

@Dao
interface DimensionDao {

    @Query("SELECT * FROM dimensions")
    fun getAll(): List<Dimension>


    @Query("SELECT * FROM dimensions WHERE dimension_name= :dimensionClass")
    fun getDimensions(dimensionClass: String): List<Dimension>

    @Query("SELECT * FROM dimensions WHERE id = :dimensionId")
    fun findById(dimensionId: Int): Dimension

    @Query("DELETE FROM dimensions")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dimension: Dimension)

    @Delete
    fun deleteAll(vararg dimension: Dimension)
}