package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Room
import android.content.Context
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class RoomArticleRepository(val context: Context) : ArticleRepository {
    private val executor: Executor = Executors.newFixedThreadPool(2)

    override fun search(query: String, handler: ArticleRepositoryResultHandler) {
        executor.execute {
            val dao = articleDao()
            val results = dao.getAllByText(query)
            for (result in results) {
                val article = buildArticle(result)
                handler.handleArticle(article)
            }
        }
    }

    fun saveArticle(article: Article) {
        val dao = articleDao()
        var element = RoomArticle()
        element.setTitle(article.getTitle())
        element.setBody(article.getBody())
        element.setImageUrl(article.getImageUrl())
        dao.insertAll(element)
    }

    private fun buildArticle(element: RoomArticle) : Article {
        val title = element.getTitle()
        val body = element.getBody()
        val imageUrl = element.getImageUrl()
        return Article(title, body, imageUrl)
    }

    private fun articleDao() : RoomArticleDao {
        val database = Room.databaseBuilder(context, LearnDatabase::class.java, "alfred-learn")
                .allowMainThreadQueries()
                .build()
        return database.articleDao()
    }

}