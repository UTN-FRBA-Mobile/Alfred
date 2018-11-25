package com.botigocontigo.alfred.google

import org.json.simple.JSONObject
import org.json.simple.JSONArray

class GoogleSearchResult (json: JSONObject) {
    private var json = json

    fun getTitle(): String {
        return json.get("title") as String
    }

    fun getDescription(): String {
        return json.get("snippet") as String
    }

    fun getTargetUrl(): String {
        return json.get("link") as String
    }

    fun getImageUrl(): String? {
        val pagemap = json.get("pagemap") as JSONObject
        val cseImage = pagemap.get("cse_image") as JSONArray
        val cseImage1 = cseImage.get(0) as JSONObject
        return cseImage1.get("src") as String
    }

}
