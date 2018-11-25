package com.botigocontigo.alfred.backend

import android.support.annotation.Dimension
import com.botigocontigo.alfred.tasks.FodaDeserealizer
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.google.gson.GsonBuilder

class FodaPostCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
    }

    fun successWithParsedJson(results: List<Dimension>){

    }

}