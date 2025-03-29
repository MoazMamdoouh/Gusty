package com.example.gusty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun getProductsDao(): FavoriteDao

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