package com.example.gusty.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.gusty.data.local.FavoriteDataBase
import com.example.gusty.data.local.alarm.AlarmEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OnActionBroadCast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TAG", "in second broadcast ")
        val request = intent.getIntExtra("request", 0)

        val androidAlarmManager  = AndroidAlarmManager(context)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(request)

        val alarmDao = FavoriteDataBase.getInstance(context).getAlarmDao()

        if(intent.action == DONE){
           androidAlarmManager.cancel(request)
            GlobalScope.launch {
                Log.i("TAG", "Done action inside the global scope ")
                alarmDao.deleteAlarmByID(request)
            }
        }else{
           androidAlarmManager.scheduler(request , 10000)
        }
    }
}