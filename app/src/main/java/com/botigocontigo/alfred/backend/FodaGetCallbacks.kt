package com.botigocontigo.alfred.backend

import android.support.annotation.Dimension
import com.botigocontigo.alfred.tasks.FodaDeserealizer
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.google.gson.GsonBuilder

class FodaGetCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Dimension::class.java, FodaDeserealizer())
        val gson = gsonBuilder.create()

        val jsonParsed = gson.fromJson(result, Array<Dimension>::class.java).toList()
        successWithParsedJson(jsonParsed)
    }

     fun successWithParsedJson(results: List<Dimension>){

     }

}