package com.botigocontigo.alfred.learn.repositories.google

import android.content.Context
import com.botigocontigo.alfred.google.GoogleSearchService
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler

class GoogleArticleRepository(context: Context) : ArticleRepository {
    private val googleSearchService: GoogleSearchService = GoogleSearchService(context)

    override fun search(query: String, handler: ArticleRepositoryResultHandler) {
        val resultsHandler = GoogleArticleResultsHandler(handler)
        googleSearchService.search(query, resultsHandler)
    }
}