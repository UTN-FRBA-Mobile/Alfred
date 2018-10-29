package com.botigocontigo.alfred

import android.appwidget.AppWidgetProvider
import android.widget.Toast
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.net.Uri

class LearnWidgetActivity : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (i in appWidgetIds.indices) {
            val currentWidgetId = appWidgetIds[i]
            val url = "http://www.tutorialspoint.com"

            // val intent = Intent(Intent.ACTION_VIEW)
            val intent = Intent(context, LearnActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse(url)

            val pending = PendingIntent.getActivity(context, 0, intent, 0)
            val views = RemoteViews(context.getPackageName(), R.layout.widget_learn)

            views.setOnClickPendingIntent(R.id.widgetImageView, pending)
            appWidgetManager.updateAppWidget(currentWidgetId, views)
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show()
        }
    }
}