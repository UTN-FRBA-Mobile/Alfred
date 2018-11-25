package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "modelos")
data class Area(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "userId")
    var userId: String?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "clients")
    var clients: String?,

    @ColumnInfo(name = "relationships")
    var relationships: String?,

    @ColumnInfo(name = "channels")
    var channels: String?,

    @ColumnInfo(name = "valueProposition")
    var valueProposition: String?,

    @ColumnInfo(name = "activities")
    var activities: String?,

    @ColumnInfo(name = "resources")
    var resources: String?,

    @ColumnInfo(name = "partners")
    var partners: String?,

    @ColumnInfo(name = "income")
    var income: String?,

    @ColumnInfo(name = "costs")
    var costs: String?

/*

    @ColumnInfo(name = "segments")
    var segments: String?,

    @ColumnInfo(name = "providers")
    var providers: String?,

    @ColumnInfo(name = "agglutinators")
    var agglutinators: String?,

    @ColumnInfo(name = "competitors")
    var competitors: String?
*/
)