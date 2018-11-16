package com.botigocontigo.alfred.backend

abstract class ApiResponseHandler {

    abstract fun success(response: String)

    fun error() {
        // nothing to do by default
    }

}
