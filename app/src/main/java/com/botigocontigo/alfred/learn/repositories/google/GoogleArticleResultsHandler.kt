package com.botigocontigo.alfred.learn.repositories.google

import com.botigocontigo.alfred.google.GoogleSearchResult
import com.botigocontigo.alfred.google.GoogleSearchResultsHandler
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler

class GoogleArticleResultsHandler(private val handler: ArticleRepositoryResultHandler) : GoogleSearchResultsHandler {

    override fun error(query: String) {
        handler.error(query)
    }

    override fun success(results: List<GoogleSearchResult>) {
        for(result in results) {
            val article = buildArticle(result)
            handler.handleArticle(article)
        }
    }

    private fun buildArticle(result: GoogleSearchResult): Article {
        val title = result.getTitle()
        val body = result.getDescription()
        val imageUrl = result.getImageUrl()
        return Article(title, body, imageUrl)
    }
}