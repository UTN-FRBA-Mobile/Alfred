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
    fun getAllByPlanId(planId: String): List<Task>

    @Query("SELECT count(1) FROM tasks WHERE plan_id = :planId")
    fun getCountTasksByPlan(planId: String): Int

    @Query("UPDATE tasks SET name = :name, frecuency_type = :frecType, "+
            "frecuency_value = :frecValue WHERE id = :id")
    fun updateTask(id: String, name: String, frecType: String, frecValue: String)

    @Query("DELETE FROM tasks")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg task: Task)

    @Delete
    fun deleteAll(vararg task: Task)

}