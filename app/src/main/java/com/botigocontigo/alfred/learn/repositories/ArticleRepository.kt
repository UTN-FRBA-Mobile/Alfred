package com.botigocontigo.alfred.learn.repositories

interface ArticleRepository {

    fun search(query: String, handler: ArticleRepositoryResultHandler)

}