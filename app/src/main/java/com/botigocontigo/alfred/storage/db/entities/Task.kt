package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String,

        @ColumnInfo(name = "name")
        var name: String?,

        @ColumnInfo(name = "frecuency_type")
        var frecType: String?,

        @ColumnInfo(name = "frecuency_value")
        var frecValue: String?,

        @ColumnInfo(name = "responsible_id")
        var responsibleId: String? = null,

        @ColumnInfo(name = "supervisor_id")
        var supervisorId: String? = null,

        @ColumnInfo(name = "completed")
        var completed: Boolean?,

        @ColumnInfo(name = "plan_id")
        var planId: String
)