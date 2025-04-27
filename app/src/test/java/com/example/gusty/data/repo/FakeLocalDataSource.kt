package com.example.gusty.data.repo

import com.example.gusty.data.local.GustyLocalDataSource
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.HomeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource(
    val favoriteList : MutableList<FavoriteEntity> = mutableListOf()
) : GustyLocalDataSource {
    override suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long {
        favoriteList.add(favoriteEntity)
        return 1
    }

    override fun getListOfFavoriteItems(): Flow<List<FavoriteEntity>> {
       return flowOf(favoriteList)
    }

    override suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity): Int {
        favoriteList.remove(favoriteEntity)
        return 1
    }

    override suspend fun insertAlarmToDataBase(alarmEntity: AlarmEntity): Long {
        TODO("Not yet implemented")
    }

    override fun getAllAlarmsFromDataBase(): Flow<List<AlarmEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeScreen(homeEntity: HomeEntity): Long {
        TODO("Not yet implemented")
    }

    override fun getHomeObj(): Flow<HomeEntity> {
        TODO("Not yet implemented")
    }
}