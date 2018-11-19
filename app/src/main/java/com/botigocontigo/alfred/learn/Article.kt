package com.botigocontigo.alfred.learn

open class Article (title: String, body: String, imageUrl: String?) {
    private var title: String = title
    private var body: String = body
    private var imageUrl: String? = imageUrl

    fun getTitle(): String {
        return title
    }

    fun getBody(): String {
        return body
    }

    fun getImageUrl(): String? {
        return imageUrl
    }
}