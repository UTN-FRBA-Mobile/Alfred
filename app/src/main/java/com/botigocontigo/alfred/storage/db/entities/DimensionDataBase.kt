package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.botigocontigo.alfred.storage.db.converters.DateConverter
import java.util.Date

@Entity(tableName = "dimensionsDataBase")
@TypeConverters(DateConverter::class)
data class DimensionDataBase(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id: Int = 0,

        @ColumnInfo(name = "descriptions")
        var name: String,

        @ColumnInfo(name = "user_id")
        var userId: String,

        @ColumnInfo(name = "dimension_name")
        var dimension_name: String,

        @ColumnInfo(name = "create_at")
        var createdDate: Date?
)