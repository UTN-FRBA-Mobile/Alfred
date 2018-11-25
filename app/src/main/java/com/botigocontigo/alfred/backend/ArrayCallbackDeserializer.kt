package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.tasks.Plan
import com.botigocontigo.alfred.tasks.PlanDeserializer
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.google.gson.GsonBuilder

abstract class ArrayCallbackDeserializer : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Plan::class.java, PlanDeserializer())
        val gson = gsonBuilder.create()

        val jsonParsed = gson.fromJson(result , Array<Plan>::class.java).toList()
        successWithParsedJson(jsonParsed)
    }

    abstract fun successWithParsedJson(results: List<Plan>)

}