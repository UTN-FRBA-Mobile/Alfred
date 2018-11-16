package com.botigocontigo.alfred.backend

class Permissions(val userId: String) {

    fun fill(apiRequest: ApiRequest) {
        apiRequest.put("userId", userId)
    }

}