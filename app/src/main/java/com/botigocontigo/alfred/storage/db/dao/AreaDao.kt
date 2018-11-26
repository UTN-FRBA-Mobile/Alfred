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
    fun findById(areaId: String): Area

    @Query("SELECT count(1) FROM modelos")
    fun getAreasCount(): Int

    @Query("DELETE FROM modelos")
    fun deleteAllRows()

    @Query("UPDATE modelos SET clients =:newClients WHERE id =:areaId")
    fun updateClients(areaId: String, newClients:String)

    @Query("DELETE FROM modelos WHERE id =:areaId")
    fun deleteModel(areaId: String)

    @Update
    fun update(entity: Area)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg area: Area)

    @Delete
    fun deleteAll(vararg area: Area)
}