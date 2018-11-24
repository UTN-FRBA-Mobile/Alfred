package com.botigocontigo.alfred.learn.repositories.actions

import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class GetAllAction : ArticleRepositoryAction {

    override fun execute(articleRepository: ArticleRepository, handler: ArticlesHandler) {
        articleRepository.getAll(handler)
    }

}