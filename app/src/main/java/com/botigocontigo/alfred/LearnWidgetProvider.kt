package com.botigocontigo.alfred

import android.appwidget.AppWidgetProvider
import android.widget.RemoteViews
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context

class LearnWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (i in appWidgetIds.indices) {
            val currentWidgetId = appWidgetIds[i]

            val urlHandler = UrlHandler("/learn")
            val intent = urlHandler.generateIntent(context)

            val pending = PendingIntent.getActivity(context, 0, intent, 0)
            val views = RemoteViews(context.getPackageName(), R.layout.widget_learn)

            views.setOnClickPendingIntent(R.id.widgetImageView, pending)
            appWidgetManager.updateAppWidget(currentWidgetId, views)
        }
    }
}