package com.example.gusty.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.gusty.R
import com.example.gusty.data.local.FavoriteDataBase
import com.example.gusty.data.local.GustyLocalDataSource
import com.example.gusty.data.remote.GustyRemoteDataSource
import com.example.gusty.data.remote.RetrofitService
import com.example.gusty.data.repo.GustyRepoImpl
import com.example.gusty.home.model.mapDtoToModel
import com.example.gusty.utilities.LocationPermission
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

const val DONE = "done"
const val SNOOZE = "Snooze"

class NotificationBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {

            val weather = runBlocking {
                Log.i("TAG", "on run block ")
                val repo = GustyRepoImpl.getInstance(
                    GustyRemoteDataSource.getInstance(RetrofitService.api),
                    GustyLocalDataSource.getInstance(
                        FavoriteDataBase.getInstance(context).getProductsDao() ,
                        FavoriteDataBase.getInstance(context).getAlarmDao()
                    )
                )
                repo.getCurrentWeather(LocationPermission.locationState.value.latitude ,
                    LocationPermission.locationState.value.longitude)
                    .map { dto -> dto.mapDtoToModel() }
                    .first()
            }



            Log.i("TAG", "in onReceive ")
            val icon = R.drawable.clear_sky_morning
            val title = weather.weather.get(0).description
            val content = " In ${weather.cityName} Temperature is ${weather.main.temperature}  "
            val request = intent?.getIntExtra("id", 0) ?: 0
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
                putExtra("request", request)
            }
            val rejectPendingIntent = PendingIntent.getBroadcast(
                context,
                request,
                rejectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val soundUri = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.weather}".toUri()
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(soundUri)
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val soundUri = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.weather}".toUri()
        val name = "Weather Notifications"
        val descriptionText = "Channel for weather updates"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            setSound(soundUri,AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
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