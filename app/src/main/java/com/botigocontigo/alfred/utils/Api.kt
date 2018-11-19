package com.botigocontigo.alfred.utils

abstract class Api(private val adapter: NetworkingAdapter) {

    abstract fun getUrl(path: String) : String

    abstract fun getQueueName() : String

    fun enqueue(methodName: String, pathWithParams : String, callbacks: AsyncTaskCallbacks<String>) {
        val queueName = getQueueName()
        val url = getUrl(pathWithParams)
        callbacks.whenTriggered() // first callback
        adapter.enqueue(queueName, methodName, url, callbacks)
    }

}