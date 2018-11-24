package com.botigocontigo.alfred.learn.repositories.actions

import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class SearchAction(private val query: String) : ArticleRepositoryAction {

    override fun execute(articleRepository: ArticleRepository, handler: ArticlesHandler) {
        articleRepository.search(query, handler)
    }

}