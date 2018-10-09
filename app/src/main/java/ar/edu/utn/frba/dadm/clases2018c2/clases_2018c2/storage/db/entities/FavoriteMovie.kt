package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class FavoriteMovie {
    @PrimaryKey
    var title: String = ""

    var year: String? = null

    var poster: String? = null
}