package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo

@Entity(tableName = "article")
class RoomArticle {

    @PrimaryKey
    private var uid: Int = 0

    @ColumnInfo(name = "title")
    private var title: String? = null

    @ColumnInfo(name = "body")
    private var body: String? = null

    @ColumnInfo(name = "image_url")
    private var imageUrl: String? = null

    fun getUid(): Int {
        return uid
    }

    fun getTitle(): String {
        return title!!
    }

    fun getBody(): String {
        return body!!
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun setUid(value: Int) {
        this.uid = value
    }

    fun setTitle(value: String) {
        this.title = value
    }

    fun setBody(value: String) {
        this.body = value

    }

    fun setImageUrl(value: String?) {
        this.imageUrl = value

    }
}