package com.botigocontigo.alfred.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.botigocontigo.alfred.utils.NetworkingAdapter

class VolleyAdapter(val context: Context) : NetworkingAdapter {
    var queues: HashMap<String, RequestQueue> = HashMap()

    override fun enqueue(queueName: String,
                         methodName: String,
                         fullUrl: String,
                         callbacks: AsyncTaskCallbacks<String>) {
        val method = getMethod(methodName)
        val stringRequest = StringRequest(method, fullUrl,
                Response.Listener<String> { response: String -> callbacks.success(response) },
                Response.ErrorListener { callbacks.error() })
        val queue = getQueue(queueName)
        queue.add(stringRequest)
    }

    private fun getQueue(queueName: String) : RequestQueue {
        val queue = queues[queueName]
        if(queue != null) return queue
        queues[queueName] = Volley.newRequestQueue(context)
        return queues[queueName]!!
    }

    private fun getMethod(methodName: String): Int {
        return when(methodName) {
            "post" -> Request.Method.POST
            "delete" -> Request.Method.DELETE
            else -> Request.Method.GET
        }
    }
}