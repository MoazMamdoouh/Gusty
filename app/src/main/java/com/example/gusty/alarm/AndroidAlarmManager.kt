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
) : AlarmScheduler{
    val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    @SuppressLint("ScheduleExactAlarm")
    override fun scheduler(item: AlarmEntity) {
        Log.i("TAG", "in scheduler ")
        val requestCode = item.id
        val intent = Intent(context , NotificationBroadCastReceiver::class.java).apply {
            putExtra("request" , requestCode)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP ,
            item.startDuration.toLong() ,
            PendingIntent.getBroadcast(
                context ,
                requestCode,
                intent ,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

    }

    override fun cancel(item: AlarmEntity) {
        Log.i("TAG", " in cancel: ")
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context ,
                item.hashCode(),
                Intent(context , NotificationBroadCastReceiver::class.java) ,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}