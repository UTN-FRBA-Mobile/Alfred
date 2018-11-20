package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.utils.Api
import com.botigocontigo.alfred.utils.ApiRequest
import com.botigocontigo.alfred.utils.NetworkingAdapter

class BotigocontigoApi(adapter: NetworkingAdapter, val permissions: Permissions) : Api(adapter) {

    // usage example:
    // api.learnQuery().call(callbacks)

    fun learnQuery(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.query")
        applyPermissions(request)
        return request
    }

    private fun applyPermissions(request: ApiRequest) {
        val userId = permissions.getUserId()
        request.put("userId", userId)
    }

    override fun getUrl(path: String): String {
        return "http://178.128.229.73:3300/$path"
    }

    override fun getQueueName() : String {
        return "botigocontigo-api"
    }

}