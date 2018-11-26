package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.botigocontigo.alfred.storage.db.converters.DateConverter
import java.util.Date

@Entity(tableName = "plans")
@TypeConverters(DateConverter::class)
data class Plan(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id: String,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "business_area")
        var businessArea: String?,

        @ColumnInfo(name = "user_id")
        var userId: String?,

        @ColumnInfo(name = "user_email")
        var userEmail: String?,

        @ColumnInfo(name = "create_at")
        var createdDate: String?
)