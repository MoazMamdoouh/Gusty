package com.example.gusty.data.local

import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.HomeEntity
import kotlinx.coroutines.flow.Flow

interface GustyLocalDataSource {
    suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long
    fun getListOfFavoriteItems(): Flow<List<FavoriteEntity>>
    suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity) : Int
    suspend fun insertAlarmToDataBase(alarmEntity: AlarmEntity) : Long
    fun getAllAlarmsFromDataBase() : Flow<List<AlarmEntity>>
    suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity) : Int
    suspend fun insertHomeScreen(homeEntity: HomeEntity) : Long
    fun getHomeObj() : Flow<HomeEntity>
}