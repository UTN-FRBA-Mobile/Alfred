package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.tasks.Plan
import com.botigocontigo.alfred.tasks.PlanDeserializer
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.google.gson.GsonBuilder

//abstract class PlansGetCallbacks : AsyncTaskCallbacks<String>() {
class PlansGetCallbacks(private val loadDB: (List<Plan>) -> Unit) : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Plan::class.java, PlanDeserializer())
        val gson = gsonBuilder.create()

        val jsonParsed = gson.fromJson(result , Array<Plan>::class.java).toList()

        loadDB(jsonParsed)

//        successWithParsedJson(jsonParsed)
    }

//    abstract fun successWithParsedJson(results: List<Plan>)
//    fun successWithParsedJson(results: List<Plan>)

}