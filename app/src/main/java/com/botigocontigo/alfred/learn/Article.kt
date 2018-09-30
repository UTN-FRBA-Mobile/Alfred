package com.botigocontigo.alfred.learn

class Article (title: String, body: String) {
    private var title: String = title
    private var body: String = body

    fun getTitle(): String {
        return title;
    }

    fun getBody(): String {
        return body;
    }
}