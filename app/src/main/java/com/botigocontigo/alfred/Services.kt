package com.botigocontigo.alfred

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.botigocontigo.alfred.backend.BotigocontigoApi
import com.botigocontigo.alfred.backend.Permissions
import com.botigocontigo.alfred.google.Credentials
import com.botigocontigo.alfred.google.GoogleApi
import com.botigocontigo.alfred.google.GoogleSearchService
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.api.ApiArticleRepository
import com.botigocontigo.alfred.learn.repositories.google.GoogleArticleRepository
import com.botigocontigo.alfred.learn.repositories.intelligent.IntelligentArticleRepository
import com.botigocontigo.alfred.learn.repositories.room.LearnDatabase
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleDao
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleRepository
import com.botigocontigo.alfred.utils.NetworkingAdapter
import com.botigocontigo.alfred.utils.VolleyAdapter

class Services(val context: Context) {

    private val networkingAdapter: NetworkingAdapter = VolleyAdapter(context)

    private fun networkingAdapter() : NetworkingAdapter {
        return networkingAdapter
    }

    fun botigocontigoApi(): BotigocontigoApi {
        return BotigocontigoApi(networkingAdapter(), currentPermissions())
    }

    private fun defaultGoogleCredentials() : Credentials {
        val key = "AIzaSyAUCMfku2xPsAr16GxrFMp90ao25bD7bOo"
        val cx = "011625570648950846187:sasexwj1n9g"
        return Credentials(key, cx)
    }

    private fun googleApi() : GoogleApi {
        val credentials = defaultGoogleCredentials()
        return GoogleApi(networkingAdapter(), credentials)
    }

    private fun googleSearchService() : GoogleSearchService {
        return GoogleSearchService(googleApi())
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE `Article` ADD url varchar")
        }
    }

    private val learnDatabase: LearnDatabase = Room.databaseBuilder(context, LearnDatabase::class.java, "alfred-learn")
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2)
            .build()

    private fun learnDatabase(): LearnDatabase {
        return learnDatabase
    }

    private fun articleDao(): RoomArticleDao {
        return learnDatabase().articleDao()
    }

    fun roomArticleRepository(): RoomArticleRepository {
        return RoomArticleRepository(articleDao())
    }

    private fun apiArticleRepository(): ArticleRepository {
        val api = botigocontigoApi()
        return ApiArticleRepository(api)
    }

    fun favoritesArticleRepository(): IntelligentArticleRepository {
        val repositories = ArrayList<ArticleRepository>()
        repositories.add(apiArticleRepository())
        repositories.add(roomArticleRepository())
        return IntelligentArticleRepository(repositories)
    }

    fun generalArticleRepository(): ArticleRepository {
        return GoogleArticleRepository(googleSearchService())
    }

    private fun currentPermissions() : Permissions {
        return Permissions(currentUser())
    }

    fun currentUser(): User {
        val pref = MyPreferences(context)
        return User(userId = pref.getUserId(), email = pref.getUserEmail())
    }

}