package com.botigocontigo.alfred.learn.repositories.room

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RoomArticleRepository(private val articleDao: RoomArticleDao) : ArticleRepository {
    var onAsyncTaskDone = { }

    override fun search(query: String, handler: ArticlesHandler) {
        doAsync {
            val results = articleDao.getAllByText(query)
            uiThread {
                onAsyncTaskDone()
                dispatch(results, handler)
            }
        }
    }

    override fun getAll(handler: ArticlesHandler) {
        doAsync {
            val results = articleDao.getAll()
            uiThread {
                onAsyncTaskDone()
                dispatch(results, handler)
            }
        }
    }

    override fun isPresent(article: Article, handler: ArticlePresentHandler) {
        doAsync {
            val url = article.link
            val count = articleDao.linkCount(url)
            uiThread {
                onAsyncTaskDone()
                handler.success(count > 0)
            }
        }
    }

    override fun upsert(article: Article) {
        val element = RoomArticle()
        element.setTitle(article.title)
        element.setDescription(article.description)
        element.setImageUrl(article.imageUrl)
        element.setLink(article.link)
        doAsync {
            onAsyncTaskDone()
            articleDao.insertAll(element)
        }
    }

    override fun delete(article: Article) {
        val link = article.link
        doAsync {
            onAsyncTaskDone()
            articleDao.deleteByLink(link)
        }
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