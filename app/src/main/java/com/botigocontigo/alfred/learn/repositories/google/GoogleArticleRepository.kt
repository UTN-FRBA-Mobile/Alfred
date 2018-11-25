package com.botigocontigo.alfred.learn.repositories.google

import com.botigocontigo.alfred.google.GoogleSearchService
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.GetAllAction

class GoogleArticleRepository(private val googleSearchService: GoogleSearchService) : ArticleRepository {

    override fun search(query: String, handler: ArticlesHandler) {
        val callbacksAdapter = GoogleSearchArticlesDispatcher(query, handler)
        googleSearchService.search(query, callbacksAdapter)
    }

    override fun getAll(handler: ArticlesHandler) {
        val action = GetAllAction()
        handler.error(action)
    }

    override fun upsert(article: Article) {
        // does nothing
    }

}