package com.botigocontigo.alfred.learn.repositories.intelligent

import android.content.Context
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleRepository

class IntelligentGoogleResultHandler(val context: Context, private val originalHandler: ArticleRepositoryResultHandler) : ArticleRepositoryResultHandler {
    private val roomRepository: RoomArticleRepository = RoomArticleRepository(context)

    override fun handleArticle(article: Article) {
        roomRepository.saveArticle(article)
        originalHandler.handleArticle(article)
    }

    override fun error(query: String) {
        roomRepository.search(query, originalHandler)
    }

}