package com.botigocontigo.alfred.learn.repositories.api

import com.botigocontigo.alfred.backend.BotigocontigoApi
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.SearchAction
import com.botigocontigo.alfred.utils.NullCallbacks

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
        val title = article.title
        val description = article.description
        val link = article.link
        val imageUrl = article.imageUrl
        val callbacks = NullCallbacks()
        api.saveFavoriteArticle(title, description, link, imageUrl).call(callbacks)
    }

    override fun delete(article: Article) {
        val link = article.link
        val callbacks = NullCallbacks()
        api.deleteFavoriteArticle(link).call(callbacks)
    }

    override fun isPresent(article: Article, handler: ArticlePresentHandler) {
        val callbacks = ApiArticlePresentAdapter(article, handler)
        api.favoriteArticles().call(callbacks)
    }

}