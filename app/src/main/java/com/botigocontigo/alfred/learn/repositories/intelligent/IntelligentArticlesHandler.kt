package com.botigocontigo.alfred.learn.repositories.intelligent

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.ArticleRepositoryAction

class IntelligentArticlesHandler(private val otherRepositories: List<ArticleRepository>,
                                 private val realHandler: ArticlesHandler) : ArticlesHandler {

    override fun searchSuccessful() {
        realHandler.searchSuccessful()
    }

    override fun handleArticle(article: Article) {
        realHandler.handleArticle(article)
    }

    override fun error(action: ArticleRepositoryAction) {
        val repository = IntelligentArticleRepository(otherRepositories)
        action.execute(repository, realHandler)
    }

}