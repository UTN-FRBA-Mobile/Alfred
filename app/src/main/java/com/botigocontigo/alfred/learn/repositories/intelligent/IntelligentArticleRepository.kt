package com.botigocontigo.alfred.learn.repositories.intelligent

import android.content.Context
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler
import com.botigocontigo.alfred.learn.repositories.google.GoogleArticleRepository
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleRepository

class IntelligentArticleRepository(val context: Context) : ArticleRepository {
    private val googleRepository: GoogleArticleRepository = GoogleArticleRepository(context)

    override fun search(query: String, handler: ArticleRepositoryResultHandler) {
        val googleHandler = IntelligentGoogleResultHandler(context, handler)
        googleRepository.search(query, googleHandler)
    }

}