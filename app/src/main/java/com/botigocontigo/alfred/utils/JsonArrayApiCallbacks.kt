package com.botigocontigo.alfred.utils

import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser

abstract class JsonArrayApiCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        var json: JSONArray? = null
        try {
            val parser = JSONParser()
            json = parser.parse(result) as JSONArray
        } catch (exception: RuntimeException) {
            error()
        }
        successWithParsedJson(json!!)
    }

    abstract fun successWithParsedJson(result: JSONArray)
}