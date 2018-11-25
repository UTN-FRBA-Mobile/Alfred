package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.Area

@Dao
interface AreaDao {
    @Query("SELECT * FROM modelos")
    fun getAll(): List<Area>

    @Query("SELECT * FROM modelos where userId= :uId")
    fun getModelsByUserId(uId: String): List<Area>

    @Query("SELECT * FROM modelos WHERE id = :areaId")
    fun findById(areaId: Int): Area

    @Query("DELETE FROM modelos")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg area: Area)

    @Delete
    fun deleteAll(vararg area: Area)
}