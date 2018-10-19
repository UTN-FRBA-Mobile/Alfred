package com.botigocontigo.alfred

class ChatBubble(val content: String, private val myMessage: Boolean) {

    fun myMessage(): Boolean {
        return myMessage
    }
}