package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [RoomArticle::class], version = 2)
abstract class LearnDatabase : RoomDatabase() {

    abstract fun articleDao(): RoomArticleDao

}