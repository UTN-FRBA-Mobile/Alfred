package com.botigocontigo.alfred

import android.appwidget.AppWidgetProvider
import android.widget.RemoteViews
import android.appwidget.AppWidgetManager
import android.content.Context

class ShortcutsWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (i in appWidgetIds.indices) {
            val currentWidgetId = appWidgetIds[i]

            val remoteViewsHandler = RemoteViewsHandler(context)

            val view = RemoteViews(context.getPackageName(), R.layout.widget_shortcuts)

            val pendingIntentLearn = remoteViewsHandler.pendingIntentFor("learn")
            view.setOnClickPendingIntent(R.id.widgetLearnImageView, pendingIntentLearn)

            val pendingIntentTasks = remoteViewsHandler.pendingIntentFor("tasks")
            view.setOnClickPendingIntent(R.id.widgetTasksImageView, pendingIntentTasks)

            val pendingIntentChat = remoteViewsHandler.pendingIntentFor("chat")
            view.setOnClickPendingIntent(R.id.widgetChatImageView, pendingIntentChat)
            appWidgetManager.updateAppWidget(currentWidgetId, view)
        }
    }
}