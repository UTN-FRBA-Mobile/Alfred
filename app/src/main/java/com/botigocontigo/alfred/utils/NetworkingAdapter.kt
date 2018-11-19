package com.botigocontigo.alfred.utils

import com.botigocontigo.alfred.utils.AsyncTaskCallbacks

interface NetworkingAdapter {

    fun enqueue(queueName: String, methodName: String, fullUrl: String, callbacks: AsyncTaskCallbacks<String>)

}