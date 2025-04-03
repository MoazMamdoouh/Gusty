package com.example.gusty.data.local

import android.util.Log
import com.example.gusty.data.local.alarm.AlarmDao
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteDao
import com.example.gusty.data.local.favorite.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyLocalDataSource private constructor(
    private val dao : FavoriteDao ,
    private val alarmDao: AlarmDao
    ) {

     suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long {
        return dao.insertItemToFavorite(favoriteEntity)
    }
    fun getListOfFavoriteItems():Flow<List<FavoriteEntity>>{
        return try {
            dao.getAllFavoriteItems()
        }catch (e:Exception){
            return flowOf()
        }
    }

    suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity) : Int {
        return try {
            dao.deleteLocationFromFavorite(favoriteEntity)
        }catch ( e : Exception){
            return -1
        }
    }

    suspend fun insertAlarmToDataBase(alarmEntity: AlarmEntity) : Long {
        return try {
            alarmDao.insertToAlarm(alarmEntity)
        }catch (e:Exception){
            Log.i("TAG", "insertAlarmToDataBase local data base  error ${e.message}")
            -1
        }
    }

    fun getAllAlarmsFromDataBase() : Flow<List<AlarmEntity>> {
        return try {
            alarmDao.getAllAlarms()
        }catch (e : Exception){
            return flowOf()
        }
    }
    suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity) : Int{
        return try {
            alarmDao.deleteAlarmFromDataBase(alarmEntity)
        }catch (e : Exception){
            -1
        }
    }
    companion object {
        private var INSTANCE: GustyLocalDataSource? = null
        fun getInstance(
            dao: FavoriteDao ,
            alarmDao: AlarmDao
        ): GustyLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val temp = GustyLocalDataSource(dao , alarmDao)
                INSTANCE = temp
                temp
            }
        }
    }
}