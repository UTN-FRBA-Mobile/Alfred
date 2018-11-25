package com.botigocontigo.alfred.learn.repositories.api

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
import com.botigocontigo.alfred.utils.JsonArrayApiCallbacks
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class ApiArticlePresentAdapter(private val article: Article,
                               private val handler: ArticlePresentHandler) : JsonArrayApiCallbacks() {

    override fun successWithParsedJson(result: JSONArray) {
        var found = false
        result.forEach { element ->
            val link = (element as JSONObject)["link"] as String
            if(link == article.link) found = true
        }
        handler.success(found)
    }

    override fun error() {
        handler.error()
    }

}