package com.botigocontigo.alfred.utils

abstract class AsyncTask<resultObjectClass> {

    fun call(callbacks: AsyncTaskCallbacks<resultObjectClass>) {
        // TODO new thread
        execute(callbacks)
    }

    abstract fun execute(callbacks: AsyncTaskCallbacks<resultObjectClass>)

}