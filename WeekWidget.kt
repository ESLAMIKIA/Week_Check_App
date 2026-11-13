
package com.example.week_check

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews

class WeekWidget : AppWidgetProvider() {

    @SuppressLint("RemoteViewLayout")
    override fun onUpdate(
        context: Context,
        manager: AppWidgetManager,
        ids: IntArray
    ) {
        val prefs = context.getSharedPreferences(
            "EvenWeekPrefs",
            Context.MODE_PRIVATE
        )

        val isEven = prefs.getBoolean("isEvenWeek", true)

        for (id in ids) {

            val views = RemoteViews(
                context.packageName,
                R.layout.week_widget
            )

            views.setTextViewText(
                R.id.widgetWeek,
                if (isEven) "زوج" else "فرد"
            )

            views.setTextViewText(
                R.id.widgetSubtitle,
                if (isEven) "EVEN WEEK" else "ODD WEEK"
            )

            views.setInt(
                R.id.widgetRoot,
                "setBackgroundColor",
                if (isEven) Color.rgb(23, 47, 73)
                else Color.rgb(57, 38, 79)
            )

            views.setTextColor(
                R.id.widgetSubtitle,
                if (isEven) Color.rgb(117, 220, 224)
                else Color.rgb(231, 169, 231)
            )

            val intent = Intent(
                context,
                MainActivity::class.java
            )

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            views.setOnClickPendingIntent(
                R.id.widgetRoot,
                pendingIntent
            )

            manager.updateAppWidget(id, views)
        }
    }
}
