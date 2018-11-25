package com.botigocontigo.alfred.utils

import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser

abstract class JsonArrayApiCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val parser = JSONParser()
        val json = parser.parse(result) as JSONArray
        successWithParsedJson(json)
    }

    abstract fun successWithParsedJson(result: JSONArray)
}