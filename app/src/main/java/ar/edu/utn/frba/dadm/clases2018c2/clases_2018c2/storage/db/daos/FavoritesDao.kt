package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities.Search

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM Search WHERE title LIKE '%' || :term || '%' ORDER BY dateOfSearch DESC LIMIT 3")
    fun getCoincidences(term: String): List<Search>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newSearch: Search)
}