package com.botigocontigo.alfred.storage.db.dao

import android.arch.persistence.room.*
import com.botigocontigo.alfred.storage.db.entities.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun findById(id: Int): Task

    @Query("SELECT * FROM tasks WHERE plan_id = :planId")
    fun getAllByPlanId(planId: Int): List<Task>

    @Query("DELETE FROM tasks")
    fun deleteAllRows()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 1 WHERE name = 'tasks'")
    fun clearPrimaryKey()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg task: Task)

    @Delete
    fun deleteAll(vararg task: Task)

}