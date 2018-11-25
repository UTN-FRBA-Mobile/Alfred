package com.botigocontigo.alfred.learn.repositories.google

import com.botigocontigo.alfred.google.GoogleSearchCallbacks
import com.botigocontigo.alfred.google.GoogleSearchResult
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.SearchAction

class GoogleSearchArticlesDispatcher(private val query: String,
                                     private val handler: ArticlesHandler) : GoogleSearchCallbacks() {

    override fun successWithParsedResults(results: List<GoogleSearchResult>) {
        handler.searchSuccessful()
        for(result in results) {
            val article = buildArticle(result)
            handler.handleArticle(article)
        }
    }

    override fun error() {
        val action = SearchAction(query)
        handler.error(action)
    }

    private fun buildArticle(result: GoogleSearchResult): Article {
        val title = result.getTitle()
        val body = result.getDescription()
        val url = result.getTargetUrl()
        val imageUrl = result.getImageUrl()
        return Article(title, body, url, imageUrl!!)
    }

}