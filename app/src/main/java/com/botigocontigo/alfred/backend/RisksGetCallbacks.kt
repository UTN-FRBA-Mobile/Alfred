package com.botigocontigo.alfred.backend


import com.botigocontigo.alfred.storage.db.entities.Risk
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.google.gson.GsonBuilder

abstract class RisksGetCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Risk::class.java, RiskDeserializer())
        val gson = gsonBuilder.create()

        val jsonParsed = gson.fromJson(result , Array<Risk>::class.java).toList()
        successWithParsedJson(jsonParsed)
    }

    abstract fun successWithParsedJson(results: List<Risk>)

}