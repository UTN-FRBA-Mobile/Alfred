package com.botigocontigo.alfred.learn

class Article (title: String, body: String, imageResourceId: Int) {
    private var title: String = title
    private var body: String = body
    private var imageResourceId: Int = imageResourceId

    fun getTitle(): String {
        return title
    }

    fun getBody(): String {
        return body
    }

    fun getImageResourceId(): Int {
        return imageResourceId
    }
}