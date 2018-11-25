package com.botigocontigo.alfred.learn.repositories.api

import com.botigocontigo.alfred.backend.BotigocontigoApi
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.SearchAction
import java.lang.RuntimeException

class ApiArticleRepository(val api: BotigocontigoApi) : ArticleRepository {

    override fun search(query: String, handler: ArticlesHandler) {
        val action = SearchAction(query)
        handler.error(action)
    }

    override fun getAll(handler: ArticlesHandler) {
        val callbacks = ApiArticlesDispatcher(handler)
        api.favoriteArticles().call(callbacks)
    }

    override fun upsert(article: Article) {
        // TODO implement
        throw RuntimeException("implementar!")
    }

}