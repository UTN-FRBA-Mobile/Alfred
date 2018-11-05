package com.botigocontigo.alfred.google

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class GoogleSearchService(context: Context) {
    val context: Context = context

    fun search(query: String, resultsHandler: GoogleSearchResultsHandler) {
        val url = generateUrl(query)
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response: String ->
                    val results = parseResults(response)
                    resultsHandler.success(results)
                },
                Response.ErrorListener {
                    resultsHandler.error()
                })
        queue.add(stringRequest)
    }

    //Note: We can use multiple APIs to obtain better results than only 10 sites
    private fun generateUrl(query: String): String {
        val key = "AIzaSyAUCMfku2xPsAr16GxrFMp90ao25bD7bOo"
        val cx = "011625570648950846187:sasexwj1n9g"
        val q = query.replace(' ', '+')
        return "https://www.googleapis.com/customsearch/v1/siterestrict?key=$key&cx=$cx&q=$q"
    }

    private fun parseResults(data: String): List<GoogleSearchResult> {
        val parser = JSONParser()
        val json = parser.parse(data) as JSONObject
        val items = json.get("items") as JSONArray
        val results = mutableListOf<GoogleSearchResult>()
        items.forEach { item ->
            val result = GoogleSearchResult(item as JSONObject)
            results.add(result)
        }
        return results
    }

}
