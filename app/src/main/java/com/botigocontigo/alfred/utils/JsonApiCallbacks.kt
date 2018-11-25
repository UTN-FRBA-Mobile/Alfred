package com.botigocontigo.alfred.utils

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.lang.RuntimeException

abstract class JsonApiCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        var json: JSONObject? = null
        try {
            val parser = JSONParser()
            json = parser.parse(result) as JSONObject
        } catch (exception: RuntimeException) {
            error()
        }
        successWithParsedJson(json!!)
    }

    abstract fun successWithParsedJson(result: JSONObject)

}