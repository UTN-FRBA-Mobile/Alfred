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

    @ColumnInfo(name = "description")
    private var description: String? = null

    @ColumnInfo(name = "image_url")
    private var imageUrl: String? = null

    @ColumnInfo(name = "url")
    private var link: String? = null

    fun getUid(): Int? {
        return uid
    }

    fun getTitle(): String {
        return title!!
    }

    fun getDescription(): String {
        return description!!
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun getLink(): String {
        return link!!
    }

    fun setUid(value: Int) {
        this.uid = value
    }

    fun setTitle(value: String) {
        this.title = value
    }

    fun setDescription(value: String) {
        this.description = value

    }

    fun setImageUrl(value: String?) {
        this.imageUrl = value
    }

    fun setLink(value: String?) {
        this.link = value
    }

}