package com.example.gusty.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.gusty.R

const val DONE = "done"
const val SNOOZE = "Snooze"

class NotificationBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            Log.i("TAG", "in onReceive ")
            val icon = intent?.getIntExtra("icon", 0) ?: R.drawable.ic_launcher_foreground
            val title = intent?.getStringExtra("Message")
            val content = intent?.getStringExtra("content")
            val request = intent?.getIntExtra("request", 0) ?: 0
            val channelId = "weather_not"
            createNotificationChannel(context, channelId)

          /*  val alertNotificationIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(
                    context,
                    0,
                    alertNotificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )*/
            val doneIntent = Intent(context, OnActionBroadCast::class.java).apply {
                action = DONE
                putExtra("Message", title)
                putExtra("icon", icon)
                putExtra("content", content)
                putExtra("request", request)
            }
            val donePendingIntent = PendingIntent.getBroadcast(
                context,
                request,
                doneIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val rejectIntent = Intent(context, OnActionBroadCast::class.java).apply {
                action = SNOOZE
                putExtra("Message", title)
                putExtra("icon", icon)
                putExtra("content", content)
                putExtra("request", request)
            }
            val rejectPendingIntent = PendingIntent.getBroadcast(
                context,
                request,
                rejectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                //.setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .addAction(R.drawable.check, DONE, donePendingIntent)
                .addAction(R.drawable.close, SNOOZE, rejectPendingIntent)


            val notification = NotificationManagerCompat.from(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    notification.notify(request, notificationBuilder.build())
                } else {
                    Log.i("TAG", "Permission POST_NOTIFICATIONS not granted!")
                }
            } else {
                notification.notify(
                    request,
                    notificationBuilder.build()
                ) // Below Android 13, no permission needed
                Log.i("TAG", "notification send without Permission POST_NOTIFICATIONS ")
            }
        }
    }
}

private fun createNotificationChannel(context: Context, channelId: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val name = "Weather Notifications"
        val descriptionText = "Channel for weather updates"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
/*val action = intent?.action
        if(action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
            Toast.makeText(context, "Wifi status changed " , Toast.LENGTH_SHORT).show()
            val cm : ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val actionNetwork : NetworkInfo? = cm.activeNetworkInfo

            val isConnected : Boolean = actionNetwork != null && actionNetwork.isConnectedOrConnecting
            if (isConnected){
                Toast.makeText(context, "NetWork Connected" , Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context, "NetWork not Connected" , Toast.LENGTH_LONG).show()
            }
        }
 */