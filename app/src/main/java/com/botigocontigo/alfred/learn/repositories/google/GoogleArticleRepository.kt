package com.botigocontigo.alfred.learn.repositories.google

import com.botigocontigo.alfred.google.GoogleSearchService
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class GoogleArticleRepository(private val googleSearchService: GoogleSearchService) : ArticleRepository {

    override fun search(query: String, articlesHandler: ArticlesHandler) {
        val callbacksAdapter = GoogleSearchArticlesDispatcher(query, articlesHandler)
        googleSearchService.search(query, callbacksAdapter)
    }

}