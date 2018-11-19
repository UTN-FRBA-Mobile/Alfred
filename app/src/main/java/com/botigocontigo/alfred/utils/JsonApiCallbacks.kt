package com.botigocontigo.alfred.utils

import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

abstract class JsonApiCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val parser = JSONParser()
        val json = parser.parse(result) as JSONObject
        successWithParsedResult(json)
    }

    abstract fun successWithParsedResult(result: JSONObject)

}