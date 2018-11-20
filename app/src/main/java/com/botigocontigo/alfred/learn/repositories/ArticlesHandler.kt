package com.botigocontigo.alfred.learn.repositories

import com.botigocontigo.alfred.learn.Article

interface ArticlesHandler {

    fun handleArticle(article: Article)
    fun error(query: String)

}