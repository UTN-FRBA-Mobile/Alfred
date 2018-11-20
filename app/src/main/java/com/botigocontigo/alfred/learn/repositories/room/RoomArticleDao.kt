package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface RoomArticleDao {

    @Query("SELECT * FROM article")
    fun getAll(): List<RoomArticle>

    @Query("SELECT * FROM article WHERE title LIKE :text OR body LIKE :text")
    fun getAllByText(text: String): List<RoomArticle>

    @Insert
    fun insertAll(vararg articles: RoomArticle)

    @Query("SELECT COUNT(1) FROM article WHERE url = :url")
    fun urlCount(url: String) : Int

    @Query("DELETE FROM article WHERE url = :url")
    fun deleteByUrl(url: String)

}