package com.botigocontigo.alfred.learn.repositories

import com.botigocontigo.alfred.learn.Article

interface ArticleRepository {

    fun search(query: String, handler: ArticlesHandler)
    fun getAll(handler: ArticlesHandler)
    fun upsert(article: Article)

}