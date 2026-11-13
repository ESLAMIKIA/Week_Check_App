
package com.example.week_check

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import java.util.Calendar

class WeekChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val prefs = context.getSharedPreferences(
            "EvenWeekPrefs",
            Context.MODE_PRIVATE
        )

        when (intent?.action) {

            ACTION_AUTO_CHANGE -> {
                val current = prefs.getBoolean("isEvenWeek", true)

                prefs.edit()
                    .putBoolean("isEvenWeek", !current)
                    .putLong(KEY_NEXT_CHANGE, 0L)
                    .apply()

                updateWidget(context)
                scheduleNextSaturday(context)
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                scheduleNextSaturday(context)
            }
        }
    }

    companion object {

        private const val ACTION_AUTO_CHANGE =
            "com.example.week_check.AUTO_WEEK_CHANGE"

        private const val KEY_NEXT_CHANGE = "nextAutoWeekChange"

        fun scheduleNextSaturday(context: Context) {

            val prefs = context.getSharedPreferences(
                "EvenWeekPrefs",
                Context.MODE_PRIVATE
            )

            val now = System.currentTimeMillis()
            val savedTime = prefs.getLong(KEY_NEXT_CHANGE, 0L)

            // بررسی تغییرهایی که هنگام خاموش بودن گوشی از دست رفته‌اند
            if (savedTime > 0L && savedTime <= now) {

                val cursor = Calendar.getInstance().apply {
                    timeInMillis = savedTime
                }

                var missedWeeks = 0

                while (cursor.timeInMillis <= now) {
                    missedWeeks++
                    cursor.add(Calendar.DAY_OF_YEAR, 7)
                }

                if (missedWeeks % 2 == 1) {
                    val current = prefs.getBoolean("isEvenWeek", true)

                    prefs.edit()
                        .putBoolean("isEvenWeek", !current)
                        .apply()

                    updateWidget(context)
                }
            }

            // محاسبه شنبه آینده، ساعت صفر
            val nextSaturday = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)



                if (timeInMillis <= now) {
                    add(Calendar.DAY_OF_YEAR, 7)
                }
            }

            prefs.edit()
                .putLong(KEY_NEXT_CHANGE, nextSaturday.timeInMillis)
                .apply()

            val alarmIntent = Intent(
                context,
                WeekChangeReceiver::class.java
            ).apply {
                action = ACTION_AUTO_CHANGE
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                3107,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextSaturday.timeInMillis,
                pendingIntent
            )
        }

        fun updateWidget(context: Context) {

            val manager = AppWidgetManager.getInstance(context)

            val component = ComponentName(
                context,
                WeekWidget::class.java
            )

            val ids = manager.getAppWidgetIds(component)

            if (ids.isNotEmpty()) {
                val updateIntent = Intent(
                    context,
                    WeekWidget::class.java
                ).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(
                        AppWidgetManager.EXTRA_APPWIDGET_IDS,
                        ids
                    )
                }

                context.sendBroadcast(updateIntent)
            }
        }
    }
}
