package com.botigocontigo.alfred.backend

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class ApiRequest(private val queue: RequestQueue,
                 private val url: String,
                 private val methodName: String) {
    var params: HashMap<String, String> = HashMap()

    // example: ApiRequest(permissions, "methods/api.query", "post")

    fun call(handler: ApiResponseHandler) {
        val stringRequest = StringRequest(getMethod(), getUrlWithParams(),
                Response.Listener<String> { response: String -> handler.success(response) },
                Response.ErrorListener { handler.error() })
        queue.add(stringRequest)
    }

    fun put(key: String, value: String) : ApiRequest {
        params[key] = value
        return this
    }

    private fun getMethod(): Int {
        return when(methodName) {
            "post" -> Request.Method.POST
            "delete" -> Request.Method.DELETE
            else -> Request.Method.GET
        }
    }

    private fun getUrlWithParams() : String {
        if(params.isEmpty()) return url
        var finalUrl = "$url?"
        params.forEach {
            finalUrl = "$finalUrl&${it.key}=${it.value}"
        }
        return finalUrl
    }
}
