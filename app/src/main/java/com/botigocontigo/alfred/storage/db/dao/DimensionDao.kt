package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.DimensionDataBase

@Dao
interface DimensionDao {

    @Query("SELECT * FROM dimensionsDataBase")
    fun getAll(): List<DimensionDataBase>


    @Query("SELECT * FROM dimensionsDataBase WHERE dimension_name= :dimensionClass")
    fun getDimensions(dimensionClass: String): List<DimensionDataBase>

    @Query("SELECT * FROM dimensionsDataBase WHERE id = :dimensionId")
    fun findById(dimensionId: Int): DimensionDataBase

    @Query("DELETE FROM dimensionsDataBase")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dimensionDataBase: DimensionDataBase)

    @Delete
    fun deleteAll(vararg dimensionDataBase: DimensionDataBase)

    @Update
    fun update(entity: DimensionDataBase)

}