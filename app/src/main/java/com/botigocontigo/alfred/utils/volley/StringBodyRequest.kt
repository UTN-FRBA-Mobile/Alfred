package com.botigocontigo.alfred.utils.volley

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class StringBodyRequest(method: Int, url: String, private val body: String, successListener: Response.Listener<String>, errorListener: Response.ErrorListener) :
        StringRequest(method, url, successListener, errorListener){

    override fun getBody(): ByteArray {
        return body.toByteArray()
    }

}