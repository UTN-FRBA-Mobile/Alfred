package com.botigocontigo.alfred.learn.repositories.api

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.GetAllAction
import com.botigocontigo.alfred.utils.JsonApiCallbacks
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class ApiArticlesDispatcher(private val handler: ArticlesHandler) : JsonApiCallbacks() {

    override fun successWithParsedJson(result: JSONObject) {
        val list = result["favs"] as JSONArray
        handler.searchSuccessful()
        list.forEach { element ->
            val article = buildArticle(element as JSONObject)
            handler.handleArticle(article)
        }
    }

    override fun error() {
        val action = GetAllAction()
        handler.error(action)
    }

    private fun buildArticle(element: JSONObject) : Article {
        val title = element["title"] as String
        val body = element["body"] as String
        val link = element["link"] as String
        val imageUrl = element["imageUrl"] as String?
        return Article(title, body, link, imageUrl)
    }

}