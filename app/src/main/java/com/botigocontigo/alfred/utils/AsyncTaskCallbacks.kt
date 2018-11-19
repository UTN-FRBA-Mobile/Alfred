package com.botigocontigo.alfred.utils

abstract class AsyncTaskCallbacks<resultObjectClass> {

    fun whenTriggered() {
        // does nothing by default
    }

    abstract fun success(result: resultObjectClass)

    fun error() {
        // does nothing by default
    }
}