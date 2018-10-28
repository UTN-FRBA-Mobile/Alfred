package com.botigocontigo.alfred.google

import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import org.json.JSONObject
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
        val key = "AIzaSyDMau9XGIXw1WZFh5U6OCfq73DQTItMHkk"
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
        for(i in 0 until items.length()) {
            val item = items.get(i) as JSONObject
            val result = GoogleSearchResult(item)
            results.add(result)
        }
        return results
    }

}
