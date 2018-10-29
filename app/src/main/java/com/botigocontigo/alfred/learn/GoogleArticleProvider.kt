package com.botigocontigo.alfred.learn

import android.content.Context
import com.botigocontigo.alfred.google.GoogleSearchResult
import com.botigocontigo.alfred.google.GoogleSearchResultsHandler
import com.botigocontigo.alfred.google.GoogleSearchService
import java.lang.RuntimeException

class GoogleArticleProvider(context: Context, articleAdapter: ArticleAdapter) : GoogleSearchResultsHandler {
    private val articleAdapter: ArticleAdapter = articleAdapter
    private val googleSearchService: GoogleSearchService = GoogleSearchService(context)

    fun search(query: String) {
        googleSearchService.search(query, this)
    }

    override fun error() {
        throw RuntimeException("Error cargando imagenes")
    }

    override fun success(results: List<GoogleSearchResult>) {
        for(result in results) {
            val article = buildArticle(result)
            articleAdapter.addArticle(article)
        }
    }

    private fun buildArticle(result: GoogleSearchResult): Article {
        val title = result.getTitle()
        val body = result.getDescription()
        val imageUrl = result.getImageUrl()
        return Article(title, body, imageUrl)
    }
}