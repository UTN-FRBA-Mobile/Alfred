package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.daos

import android.arch.persistence.room.*
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities.Favorite
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities.Search

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newSearch: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM Favorite")
    fun find_all(): List<Favorite>
}