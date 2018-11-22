package com.botigocontigo.alfred.learn

import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks

class LearnQueryCallbacks(val adapter: ArticleAdapter) : AsyncTaskCallbacks<String>() {

    override fun success(query: String) {
        val context = adapter.context
        val articleRepository = Services(context).intelligentArticleRepository()
        articleRepository.search(query, adapter)
    }

}