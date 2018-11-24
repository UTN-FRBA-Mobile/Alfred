package com.botigocontigo.alfred.learn.repositories.room

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class RoomArticleRepository(private val articleDao: RoomArticleDao) : ArticleRepository {
    //private val executor: Executor = Executors.newFixedThreadPool(2)

    override fun search(query: String, handler: ArticlesHandler) {
        //executor.execute {
            val results = articleDao.getAllByText(query)
            handler.searchSuccessful()
            for (result in results) {
                val article = buildArticle(result)
                handler.handleArticle(article)
            }
        //}
    }

    fun fetchAll(handler: ArticlesHandler) {
        //executor.execute {
            val results = articleDao.getAll()
            for (result in results) {
                val article = buildArticle(result)
                handler.handleArticle(article)
            }
        //}
    }

    fun isPresent(article: Article) : Boolean {
        val url = article.link
        val count = articleDao.linkCount(url)
        return count > 0
    }

    fun saveArticle(article: Article) {
        var element = RoomArticle()
        element.setTitle(article.title)
        element.setDescription(article.description)
        element.setImageUrl(article.imageUrl)
        element.setLink(article.link)
        articleDao.insertAll(element)
    }

    private fun buildArticle(element: RoomArticle) : Article {
        val title = element.getTitle()
        val body = element.getDescription()
        val imageUrl = element.getImageUrl()
        val url = element.getLink()
        return Article(title, body, imageUrl!!, url)
    }

    fun deleteArticle(article: Article) {
        val url = article.link
        articleDao.deleteByLink(url)
    }

}