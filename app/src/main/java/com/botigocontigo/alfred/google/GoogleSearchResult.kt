package com.botigocontigo.alfred.google

import org.json.simple.JSONObject
import org.json.simple.JSONArray

class GoogleSearchResult (json: JSONObject) {
    private var json = json

    fun getTitle(): String {
        val input = json["title"] as String
        return safeString(input)
    }

    fun getDescription(): String {
        val input = json["snippet"] as String
        return safeString(input)
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

    private fun safeString(input: String) : String {
        val regex = Regex("[^A-Za-z0-9]")
        return input.replace(regex, " ")
    }

}
