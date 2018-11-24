package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.Area

@Dao
interface AreaDao {
    @Query("SELECT * FROM areas")
    fun getAll(): List<Area>

    @Query("SELECT * FROM areas WHERE id = :areaId")
    fun findById(areaId: Int): Area

    @Query("DELETE FROM areas")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg area: Area)

    @Delete
    fun deleteAll(vararg area: Area)
}