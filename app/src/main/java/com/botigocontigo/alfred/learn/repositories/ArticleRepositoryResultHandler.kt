package com.botigocontigo.alfred.learn.repositories

import com.botigocontigo.alfred.learn.Article

interface ArticleRepositoryResultHandler {

    fun handleArticle(article: Article)
    fun error(query: String)

}