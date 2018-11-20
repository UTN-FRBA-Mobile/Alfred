package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "plans")
data class Plan(
        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "business_area")
        var business_area: String?,

        @ColumnInfo(name = "user_id")
        var userId: String?,

        @ColumnInfo(name = "user_email")
        var userEmail: String?,

        @ColumnInfo(name = "create_at")
        var createdDate: Int?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}