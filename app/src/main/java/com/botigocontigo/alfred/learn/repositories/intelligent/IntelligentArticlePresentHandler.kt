package com.botigocontigo.alfred.learn.repositories.intelligent

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
import com.botigocontigo.alfred.learn.repositories.ArticleRepository

class IntelligentArticlePresentHandler(private val article: Article,
                                       private val otherRepositories: List<ArticleRepository>,
                                       private val realHandler: ArticlePresentHandler) : ArticlePresentHandler {

    override fun success(isPresent: Boolean) {
        realHandler.success(isPresent)
    }

    override fun error() {
        if(otherRepositories.isEmpty()) {
            realHandler.error()
        } else {
            val repository = IntelligentArticleRepository(otherRepositories)
            repository.isPresent(article, realHandler)
        }
    }

}