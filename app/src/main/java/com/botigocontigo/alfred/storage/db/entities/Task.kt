package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
        @ColumnInfo(name = "name")
        var name: String?,

        @ColumnInfo(name = "frecuency_type")
        var frecType: String?,

        @ColumnInfo(name = "frecuency_value")
        var frecValue: Int?,

        @ColumnInfo(name = "responsible_id")
        var responsibleId: String?,

        @ColumnInfo(name = "supervisor_id")
        var supervisorId: String?,

        @ColumnInfo(name = "completed")
        var completed: Boolean?,

        @ColumnInfo(name = "plan_id")
        var planId: Int?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}