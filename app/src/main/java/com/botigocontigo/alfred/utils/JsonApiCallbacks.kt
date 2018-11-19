package com.botigocontigo.alfred.utils

import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

abstract class JsonApiCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val parser = JSONParser()
        val json = parser.parse(result) as JSONObject
        successWithParsedJson(json)
    }

    abstract fun successWithParsedJson(result: JSONObject)

}