package com.botigocontigo.alfred.google

import com.github.kittinunf.fuel.Fuel
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class GoogleSearchService {

    fun search(query: String, resultsHandler: GoogleSearchResultsHandler) {
        val url = generateUrl(query)
        Fuel.get(url).responseString { request, response, result ->
            result.fold({
                data ->
                val results = parseResults(data)
                resultsHandler.success(results)
            }, {
                error ->
                resultsHandler.error()
            })
        }
    }

    private fun generateUrl(query: String): String {
        val key = "AIzaSyAUCMfku2xPsAr16GxrFMp90ao25bD7bOo"
        val cx = "011625570648950846187:sasexwj1n9g"
        val q = query.replace(' ', '+')
        return "https://www.googleapis.com/customsearch/v1/siterestrict?key=$key&cx=$cx&q=$q"
    }

    private fun parseResults(data: String): List<GoogleSearchResult> {
        val parser = JSONParser()
        val json = parser.parse(data) as JSONObject
        val queries = json.get("queries") as JSONObject
        val items = queries.get("items") as JSONArray
        val results = mutableListOf<GoogleSearchResult>()
        items.forEach { item ->
            val result = GoogleSearchResult(item as JSONObject)
            results.add(result)
        }
        return results
    }

}
