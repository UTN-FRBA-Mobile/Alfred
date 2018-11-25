package com.botigocontigo.alfred.utils

interface NetworkingAdapter {

    fun enqueue(queueName: String, methodName: String, fullUrl: String, callbacks: AsyncTaskCallbacks<String>)
    fun enqueue(queueName: String, methodName: String, fullUrl: String, body: String, callbacks: AsyncTaskCallbacks<String>)

}