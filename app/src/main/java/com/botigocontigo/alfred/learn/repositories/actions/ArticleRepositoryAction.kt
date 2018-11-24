package com.botigocontigo.alfred.learn.repositories.actions

import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

interface ArticleRepositoryAction {

    fun execute(articleRepository: ArticleRepository, handler: ArticlesHandler)

}