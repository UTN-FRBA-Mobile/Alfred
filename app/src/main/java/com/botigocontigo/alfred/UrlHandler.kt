package com.botigocontigo.alfred

import android.content.Context
import android.content.Intent
import android.net.Uri

class UrlHandler(private val relativeUrl: String) {

    private fun getUri(): Uri {
        val url = "http://alfred.botigocontigo.com$relativeUrl"
        return Uri.parse(url)
    }

    fun generateIntent(context: Context): Intent {
        val intent = Intent(context, MenuActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = getUri()
        return intent
    }

}