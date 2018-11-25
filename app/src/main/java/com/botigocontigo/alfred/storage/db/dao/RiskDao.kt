package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.Risk

@Dao
interface RiskDao {
    @Query("SELECT * FROM RIESGOS")
    fun getAll(): List<Risk>

    @Query("DELETE FROM RIESGOS")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg risk: Risk)

    @Delete
    fun deleteAll(vararg risk: Risk)
}