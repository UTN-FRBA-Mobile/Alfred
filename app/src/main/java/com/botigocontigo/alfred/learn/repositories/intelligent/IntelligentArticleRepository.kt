package com.botigocontigo.alfred.learn.repositories.intelligent

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
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

    override fun isPresent(article: Article, handler: ArticlePresentHandler) {
        val firstRepository = articleRepositories.first()
        val otherRepositories = articleRepositories.drop(1)
        val intelligentArticlesHandler = IntelligentArticlePresentHandler(article, otherRepositories, handler)
        firstRepository.isPresent(article, intelligentArticlesHandler)
    }

    override fun upsert(article: Article) {
        articleRepositories.forEach { articleRepository ->
            articleRepository.upsert(article)
        }
    }

    override fun delete(article: Article) {
        articleRepositories.forEach { articleRepository ->
            articleRepository.delete(article)
        }
    }

}