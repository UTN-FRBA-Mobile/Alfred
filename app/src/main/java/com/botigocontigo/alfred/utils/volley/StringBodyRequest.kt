package com.botigocontigo.alfred.utils.volley

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.nio.charset.Charset


class StringBodyRequest(method: Int, url: String, private val body: String, successListener: Response.Listener<String>, errorListener: Response.ErrorListener) :
        StringRequest(method, url, successListener, errorListener){

    override fun getBody(): ByteArray {
        return body.toByteArray(Charset.forName("utf-8"))
    }

    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }

}