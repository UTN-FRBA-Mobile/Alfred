package com.botigocontigo.alfred.backend

import android.util.Log
import com.botigocontigo.alfred.storage.db.entities.Area
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.google.gson.GsonBuilder

//abstract class AreasGetCallbacks : AsyncTaskCallbacks<String>() {
class AreasGetCallbacks(private val loadDB: (List<Area>) -> Unit) : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Area::class.java, AreasDeserializer())
        val gson = gsonBuilder.create()

        val jsonParsed = gson.fromJson(result , Array<Area>::class.java).toList()
        Log.i("Resultado Json Areas", result)

        loadDB(jsonParsed)
//        successWithParsedJson(jsonParsed)
    }

//    abstract fun successWithParsedJson(results: List<Area>)

}