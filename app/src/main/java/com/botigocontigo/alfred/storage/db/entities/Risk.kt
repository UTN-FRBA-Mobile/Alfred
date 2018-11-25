package com.botigocontigo.alfred.storage.db.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "RIESGOS")
data class Risk(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "ID")
        var id: Int?,

        @ColumnInfo(name = "DESCRIPCION")
        var descripcion: String?,

        @ColumnInfo(name = "PROB_OCURRENCIA")
        var pDeOcurrecia: String?,

        @ColumnInfo(name = "IMPACTO")
        var impacto: String?,

        @ColumnInfo(name = "CAP_DETECCION")
        var cDeDeteccion: String)