package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo

@Entity(tableName = "article")
class RoomArticle {

    @PrimaryKey(autoGenerate = true)
    private var uid: Int? = null

    @ColumnInfo(name = "title")
    private var title: String? = null

    @ColumnInfo(name = "body")
    private var body: String? = null

    @ColumnInfo(name = "image_url")
    private var imageUrl: String? = null

    @ColumnInfo(name = "url")
    private var url: String? = null

    fun getUid(): Int? {
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

    fun getUrl(): String {
        return url!!
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

    fun setUrl(value: String?) {
        this.url = value
    }

}