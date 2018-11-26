package com.botigocontigo.alfred.storage.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.botigocontigo.alfred.storage.db.dao.*
import com.botigocontigo.alfred.storage.db.entities.*


@Database(entities = [Plan::class, Task::class, Area::class, DimensionDataBase::class, Risk::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun planDao(): PlanDao
    abstract fun taskDao(): TaskDao
    abstract fun areaDao(): AreaDao
    abstract fun dimensionDao(): DimensionDao
    abstract fun riskDao(): RiskDao

    companion object {
        private var db: AppDatabase? = null
        private val DB_NAME = "database-alfred"

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
            }

            return db as AppDatabase
        }
    }

}