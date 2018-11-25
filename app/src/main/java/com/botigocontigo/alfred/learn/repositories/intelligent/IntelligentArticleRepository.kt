package com.botigocontigo.alfred.learn.repositories.intelligent

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class IntelligentArticleRepository(private val articleRepositories: List<ArticleRepository>) : ArticleRepository {

    override fun search(query: String, handler: ArticlesHandler) {
        val firstRepository = articleRepositories.first()
        val otherRepositories = articleRepositories.drop(1)
        val intelligentArticlesHandler = IntelligentArticlesHandler(otherRepositories, handler)
        firstRepository.search(query, intelligentArticlesHandler)
    }

    override fun getAll(handler: ArticlesHandler) {
        val firstRepository = articleRepositories.first()
        val otherRepositories = articleRepositories.drop(1)
        val intelligentArticlesHandler = IntelligentArticlesHandler(otherRepositories, handler)
        firstRepository.getAll(intelligentArticlesHandler)
    }

    override fun upsert(article: Article) {
        // does nothing
    }

}