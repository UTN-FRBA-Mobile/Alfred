package com.botigocontigo.alfred.google

import com.botigocontigo.alfred.utils.ApiRequest

class Credentials(private val key: String, private val cx: String) {

    // key "AIzaSyAUCMfku2xPsAr16GxrFMp90ao25bD7bOo"
    // cx "011625570648950846187:sasexwj1n9g"

    fun apply(request: ApiRequest) {
        request.put("key", key)
        request.put("cx", cx)
    }

}