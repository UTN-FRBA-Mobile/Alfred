package com.botigocontigo.alfred.utils

abstract class AsyncTaskCallbacks<resultObjectClass> {

    open fun whenTriggered() {
        // does nothing by default
    }

    abstract fun success(result: resultObjectClass)

    open fun error() {
        // does nothing by default
    }

}