package com.example.gusty.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.gusty.data.local.alarm.AlarmEntity

class AndroidAlarmManager(
    private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduler(id: Int, time: Long) {
        Log.i("TAG", "in scheduler ")
        if(time <= System.currentTimeMillis()){
            Log.i("TAG", "scheduler time is in the past ")
        }
        val intent = Intent(context, NotificationBroadCastReceiver::class.java).apply {
            putExtra("id", id)
            Log.i("TAG", "scheduler intent done  ")
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(id : Int) {
        Log.i("TAG", " in cancel: ")
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id,
                Intent(context, NotificationBroadCastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}