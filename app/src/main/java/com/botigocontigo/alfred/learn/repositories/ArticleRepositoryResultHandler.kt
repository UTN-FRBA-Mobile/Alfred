package com.botigocontigo.alfred.learn.repositories

import com.botigocontigo.alfred.learn.Article
import java.lang.RuntimeException

abstract class ArticleRepositoryResultHandler {

    abstract fun handleArticle(article: Article)

    open fun error(query: String) {
        throw RuntimeException("Error fetching articles of query: $query")
    }

}