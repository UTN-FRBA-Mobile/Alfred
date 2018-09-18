package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.converters.DateConverter
import java.util.*

@Entity
@TypeConverters(DateConverter::class)
class Favorite(@PrimaryKey var title: String = "", var year: String? = null, var poster: String? = null, var metascore: String? = null) {
    var dateOfFavorite: Date? = Date()

}