package com.botigocontigo.alfred.learn.repositories.room

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class RoomArticleRepository(private val articleDao: RoomArticleDao) : ArticleRepository {
    //private val executor: Executor = Executors.newFixedThreadPool(2)

    override fun search(query: String, handler: ArticlesHandler) {
        //executor.execute {
            val results = articleDao.getAllByText(query)
            dispatch(results, handler)
        //}
    }

    override fun getAll(handler: ArticlesHandler) {
        val results = articleDao.getAll()
        dispatch(results, handler)
    }

    fun isPresent(article: Article) : Boolean {
        val url = article.link
        val count = articleDao.linkCount(url)
        return count > 0
    }

    override fun upsert(article: Article) {
        val element = RoomArticle()
        element.setTitle(article.title)
        element.setDescription(article.description)
        element.setImageUrl(article.imageUrl)
        element.setLink(article.link)
        articleDao.insertAll(element)
    }

    fun deleteArticle(article: Article) {
        val link = article.link
        articleDao.deleteByLink(link)
    }

    private fun dispatch(roomArticles: List<RoomArticle>, handler: ArticlesHandler) {
        handler.searchSuccessful()
        for (roomArticle in roomArticles) {
            val article = buildArticle(roomArticle)
            handler.handleArticle(article)
        }
    }

    private fun buildArticle(roomArticle: RoomArticle) : Article {
        val title = roomArticle.getTitle()
        val body = roomArticle.getDescription()
        val link = roomArticle.getLink()
        val imageUrl = roomArticle.getImageUrl()
        return Article(title, body, link, imageUrl!!)
    }

}