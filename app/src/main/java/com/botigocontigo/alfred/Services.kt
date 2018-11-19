package com.botigocontigo.alfred

import android.arch.persistence.room.Room
import android.content.Context
import com.botigocontigo.alfred.backend.BotigocontigoApi
import com.botigocontigo.alfred.backend.Permissions
import com.botigocontigo.alfred.google.Credentials
import com.botigocontigo.alfred.google.GoogleApi
import com.botigocontigo.alfred.google.GoogleSearchService
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.google.GoogleArticleRepository
import com.botigocontigo.alfred.learn.repositories.intelligent.IntelligentArticleRepository
import com.botigocontigo.alfred.learn.repositories.room.LearnDatabase
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleDao
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleRepository
import com.botigocontigo.alfred.utils.NetworkingAdapter
import com.botigocontigo.alfred.utils.VolleyAdapter

class Services(private val context: Context) {

    private val networkingAdapter: NetworkingAdapter = VolleyAdapter(context)

    private fun networkingAdapter() : NetworkingAdapter {
        return networkingAdapter
    }

    fun botigocontigoApi(permissions: Permissions): BotigocontigoApi {
        return BotigocontigoApi(networkingAdapter(), permissions)
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

    fun googleArticleRepository() : GoogleArticleRepository {
        return GoogleArticleRepository(googleSearchService())
    }

    private val learnDatabase: LearnDatabase = Room.databaseBuilder(context, LearnDatabase::class.java, "alfred-learn")
            .allowMainThreadQueries()
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

    fun intelligentArticleRepository(): IntelligentArticleRepository {
        val repositories = ArrayList<ArticleRepository>()
        repositories.add(googleArticleRepository())
        repositories.add(roomArticleRepository())
        return IntelligentArticleRepository(repositories)
    }

}