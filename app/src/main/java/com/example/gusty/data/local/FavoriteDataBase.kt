package com.example.gusty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gusty.data.local.alarm.AlarmDao
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteDao
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.CurrentWeatherTypeConvert
import com.example.gusty.data.local.home.HomeDao
import com.example.gusty.data.local.home.HomeEntity
import com.example.gusty.data.local.home.HourlyAndDailyTypeConvert

@Database(entities = [FavoriteEntity::class , AlarmEntity::class , HomeEntity::class], version = 1)
@TypeConverters(CurrentWeatherTypeConvert::class , HourlyAndDailyTypeConvert::class)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun getProductsDao(): FavoriteDao
    abstract fun getAlarmDao() : AlarmDao
    abstract fun getHomeDao() : HomeDao
    companion object{
        @Volatile
        private var instance: FavoriteDataBase? = null
        fun getInstance(context: Context): FavoriteDataBase {
            return instance ?: synchronized(this){
                val INSTANCE = Room.databaseBuilder(context, FavoriteDataBase::class.java
                    , "room-db").build()
                instance = INSTANCE
                INSTANCE
            }
        }
    }
}