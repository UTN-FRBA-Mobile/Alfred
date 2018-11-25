package com.botigocontigo.alfred.learn.repositories

interface ArticlePresentHandler {

    fun success(isPresent: Boolean)
    fun error()

}