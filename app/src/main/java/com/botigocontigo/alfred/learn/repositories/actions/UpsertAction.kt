package com.botigocontigo.alfred.learn.repositories.actions

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler

class UpsertAction(private val article: Article) : ArticleRepositoryAction {

    override fun execute(articleRepository: ArticleRepository, handler: ArticlesHandler) {
        articleRepository.upsert(article)
    }

}