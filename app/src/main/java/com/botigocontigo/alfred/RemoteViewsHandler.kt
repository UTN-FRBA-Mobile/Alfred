package com.botigocontigo.alfred

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri

class RemoteViewsHandler(private val context: Context) {

    fun pendingIntentFor(relativeUrl: String): PendingIntent {
        val intent = generateIntent()
        intent.data = getUri(relativeUrl)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getUri(relativeUrl: String): Uri {
        val url = "http://alfred.botigocontigo.com/$relativeUrl"
        return Uri.parse(url)
    }

    private fun generateIntent(): Intent {
        val intent = Intent(context, MenuActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

}