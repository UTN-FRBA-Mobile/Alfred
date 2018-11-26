package com.botigocontigo.alfred

class User (
        var name: String? = null,
        var userId: String? = null,
        var email: String? = null
) {
    fun getNickname(): String {
        return "Max Power"
    }

}