package com.botigocontigo.alfred.utils

open class ApiRequest(private val api: Api,
                      private val methodName: String,
                      private val path: String) : AsyncTask<String>() {

    var params: HashMap<String, String> = HashMap()

    override fun execute(callbacks: AsyncTaskCallbacks<String>) {
        val pathWithParams = getPathWithParams()
        api.enqueue(methodName, pathWithParams, callbacks)
    }

    fun put(key: String, value: String) : ApiRequest {
        params[key] = value
        return this
    }

    private fun getPathWithParams() : String {
        if(params.isEmpty()) return path
        val paramsList = params.map { "${it.key}=${it.value}" }
        val joinedParams = paramsList.joinToString("&")
        return "$path?$joinedParams"
    }

}
