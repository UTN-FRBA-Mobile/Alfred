package com.botigocontigo.alfred.utils

class NullCallbacks : AsyncTaskCallbacks<String>() {

    override fun success(result: String) {
        // nothing to do; this is a null object
    }

}