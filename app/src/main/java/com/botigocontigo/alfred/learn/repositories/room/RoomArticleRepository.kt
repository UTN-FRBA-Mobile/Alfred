package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Room
import android.content.Context
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler

class RoomArticleRepository(val context: Context) : ArticleRepository {

    override fun search(query: String, handler: ArticleRepositoryResultHandler) {
        val dao = articleDao()
        val results = dao.getAllByText(query)
        for(result in results) {
            val article = buildArticle(result)
            handler.handleArticle(article)
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
        val database = Room.databaseBuilder(context, LearnDatabase::class.java, "alfred-learn").build()
        return database.articleDao()
    }

}