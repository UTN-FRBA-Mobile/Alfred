package com.botigocontigo.alfred.learn.repositories.google

import com.botigocontigo.alfred.google.GoogleSearchCallbacks
import com.botigocontigo.alfred.google.GoogleSearchResult
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class GoogleSearchArticlesDispatcher(private val query: String,
                                     private val articlesHandler: ArticlesHandler) : GoogleSearchCallbacks() {

    override fun successWithParsedResults(results: List<GoogleSearchResult>) {
        articlesHandler.searchSuccessful()
        for(result in results) {
            val article = buildArticle(result)
            articlesHandler.handleArticle(article)
        }
    }

    override fun error() {
        articlesHandler.error(query)
    }

    private fun buildArticle(result: GoogleSearchResult): Article {
        val title = result.getTitle()
        val body = result.getDescription()
        val url = result.getTargetUrl()
        val imageUrl = result.getImageUrl()
        return Article(title, body, url, imageUrl!!)
    }

}