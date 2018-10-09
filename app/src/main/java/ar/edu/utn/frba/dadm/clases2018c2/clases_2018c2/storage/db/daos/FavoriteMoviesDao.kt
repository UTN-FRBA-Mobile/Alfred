package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.storage.db.entities.FavoriteMovie

@Dao
interface FavoriteMoviesDao {
    @Query("SELECT * FROM FavoriteMovie")
    fun getAll() : List<FavoriteMovie>

    @Query("SELECT * FROM FavoriteMovie WHERE title = :title LIMIT 1")
    fun getByTitle(title: String): FavoriteMovie

    @Insert
    fun insert(movie: FavoriteMovie)

    @Delete
    fun delete(movie: FavoriteMovie)
}