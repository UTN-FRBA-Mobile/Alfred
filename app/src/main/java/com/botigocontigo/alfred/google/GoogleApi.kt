package com.botigocontigo.alfred.google

import com.botigocontigo.alfred.utils.Api
import com.botigocontigo.alfred.utils.ApiRequest
import com.botigocontigo.alfred.utils.NetworkingAdapter

class GoogleApi(adapter: NetworkingAdapter, private val credentials: Credentials) : Api(adapter) {

    fun siteRestrictSearch(q: String): ApiRequest {
        val request = ApiRequest(this, "get", "customsearch/v1/siterestrict")
        credentials.apply(request)
        request.put("q", q)
        return request
    }

    private fun applyCredentials(request: ApiRequest) {
        request.put("key", "AIzaSyAUCMfku2xPsAr16GxrFMp90ao25bD7bOo")
        request.put("cx", "011625570648950846187:sasexwj1n9g")
    }

    override fun getQueueName(): String {
        return "google-api"
    }

    override fun getUrl(path: String): String {
        return "https://www.googleapis.com/$path"
    }

}