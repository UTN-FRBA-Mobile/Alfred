package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long {
        return (date?.time)!!.toLong()
    }
}
