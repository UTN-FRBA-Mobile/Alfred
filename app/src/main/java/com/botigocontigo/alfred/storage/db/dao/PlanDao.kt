package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.Plan

@Dao
interface PlanDao {

    @Query("SELECT * FROM plans")
    fun getAll(): List<Plan>

    @Query("SELECT * FROM plans WHERE id = :planId")
    fun findById(planId: Int): Plan

    @Query("DELETE FROM plans")
    fun deleteAllRows()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 1 WHERE name = 'plans'")
    fun clearPrimaryKey();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg plan: Plan)

    @Delete
    fun deleteAll(vararg plan: Plan)
}