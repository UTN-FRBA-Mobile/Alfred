package com.botigocontigo.alfred.learn.repositories.room

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.utils.executors.AnkoExecutor
import com.botigocontigo.alfred.utils.executors.AnkoExecutorFactory
import com.botigocontigo.alfred.utils.executors.Executor
import com.botigocontigo.alfred.utils.executors.ExecutorFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RoomArticleRepository(private val articleDao: RoomArticleDao) : ArticleRepository {
    var executorFactory: ExecutorFactory = AnkoExecutorFactory()

    override fun search(query: String, handler: ArticlesHandler) {
        executorFactory.create<List<RoomArticle>>().async {
            articleDao.getAllByText(query)
        }.sync {
            dispatch(it, handler)
        }.run()
    }

    override fun getAll(handler: ArticlesHandler) {
        executorFactory.create<List<RoomArticle>>().async {
            articleDao.getAll()
        }.sync {
            dispatch(it, handler)
        }.run()
    }

    override fun isPresent(article: Article, handler: ArticlePresentHandler) {
        executorFactory.create<Int>().async {
            val url = article.link
            articleDao.linkCount(url)
        }.sync {
            handler.success(it > 0)
        }.run()
    }

    override fun upsert(article: Article) {
        val element = RoomArticle()
        element.setTitle(article.title)
        element.setDescription(article.description)
        element.setImageUrl(article.imageUrl)
        element.setLink(article.link)
        executorFactory.create<Unit>().async {
            articleDao.insertAll(element)
        }.run()
    }

    override fun delete(article: Article) {
        val link = article.link
        executorFactory.create<Unit>().async {
            articleDao.deleteByLink(link)
        }.run()
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