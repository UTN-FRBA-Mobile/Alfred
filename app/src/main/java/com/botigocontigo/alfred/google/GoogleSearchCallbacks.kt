package com.botigocontigo.alfred.google

import com.botigocontigo.alfred.utils.JsonApiCallbacks
import org.json.simple.JSONArray
import org.json.simple.JSONObject

abstract class GoogleSearchCallbacks : JsonApiCallbacks() {

    override fun successWithParsedJson(json: JSONObject) {
        val items = json["items"] as JSONArray
        val results = mutableListOf<GoogleSearchResult>()
        items.forEach { item ->
            val result = GoogleSearchResult(item as JSONObject)
            results.add(result)
        }
        successWithParsedResults(results)
    }

    abstract fun successWithParsedResults(results: List<GoogleSearchResult>)

}