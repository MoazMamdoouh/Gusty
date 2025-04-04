package com.example.gusty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gusty.data.local.alarm.AlarmDao
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteDao
import com.example.gusty.data.local.favorite.FavoriteEntity

@Database(entities = [FavoriteEntity::class , AlarmEntity::class], version = 1)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun getProductsDao(): FavoriteDao
    abstract fun getAlarmDao() : AlarmDao
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