package com.botigocontigo.alfred.backend

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class Api(private val context: Context, private val permissions: Permissions) {
    private lateinit var queue : RequestQueue

    fun learnQuery(): ApiRequest {
        return createRequest("methods/api.query", "post")
    }

    private fun createRequest(relativePath: String, methodName: String) : ApiRequest {
        val apiRequest = ApiRequest(getQueue(), getUrl(relativePath), methodName)
        permissions.fill(apiRequest)
        return apiRequest
    }

    private fun getUrl(relativePath: String): String {
        return "http://178.128.229.73:3300/$relativePath"
    }

    private fun getQueue(): RequestQueue {
        if(queue != null) return queue
        queue = Volley.newRequestQueue(context)
        return queue
    }

}