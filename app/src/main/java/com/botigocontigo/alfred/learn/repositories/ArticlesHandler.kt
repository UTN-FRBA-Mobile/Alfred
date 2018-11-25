package com.botigocontigo.alfred.learn.repositories

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.actions.ArticleRepositoryAction

interface ArticlesHandler {

    fun searchSuccessful()
    fun handleArticle(article: Article)
    fun error(action: ArticleRepositoryAction)

}