package com.botigocontigo.alfred.learn

import com.botigocontigo.alfred.utils.AsyncTaskCallbacks

class LearnQueryCallbacks(private val learnFragment: LearnFragment) : AsyncTaskCallbacks<String>() {

    override fun success(query: String) {
        learnFragment.adaptativeQuerySuccess(query)
    }

    override fun error() {
        learnFragment.adaptativeQueryError()
    }

}