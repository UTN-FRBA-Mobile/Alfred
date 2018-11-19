package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.utils.AsyncTaskCallbacks

class Api(private val adapter: NetworkingAdapter, private val permissions: Permissions) {

    // usage example:
    // api.learnQuery().call(callbacks)

    fun learnQuery(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.query")
        request.applyPermissions(permissions)
        return request
    }

    private fun getUrl(path: String): String {
        return "http://178.128.229.73:3300/$path"
    }

    fun enqueue(methodName: String, pathWithParams : String, callbacks: AsyncTaskCallbacks<String>) {
        val url = getUrl(pathWithParams)
        callbacks.whenTriggered() // first callback
        adapter.enqueue("botigocontigo-api", methodName, url, callbacks)
    }

}