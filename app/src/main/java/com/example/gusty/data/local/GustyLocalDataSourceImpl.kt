package com.example.gusty.data.local

import android.util.Log
import com.example.gusty.data.local.alarm.AlarmDao
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteDao
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.HomeDao
import com.example.gusty.data.local.home.HomeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyLocalDataSourceImpl private constructor(
    private val dao : FavoriteDao ,
    private val alarmDao: AlarmDao ,
    private val homeDao: HomeDao
    )  : GustyLocalDataSource{

    override suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long {
        return dao.insertItemToFavorite(favoriteEntity)
    }
    override fun getListOfFavoriteItems():Flow<List<FavoriteEntity>>{
        return try {
            dao.getAllFavoriteItems()
        }catch (e:Exception){
            return flowOf()
        }
    }

    override suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity) : Int {
        return try {
            dao.deleteLocationFromFavorite(favoriteEntity)
        }catch ( e : Exception){
            return -1
        }
    }

    override suspend fun insertAlarmToDataBase(alarmEntity: AlarmEntity) : Long {
        return try {
            alarmDao.insertToAlarm(alarmEntity)
        }catch (e:Exception){
            Log.i("TAG", "insertAlarmToDataBase local data base  error ${e.message}")
            -1
        }
    }

    override fun getAllAlarmsFromDataBase() : Flow<List<AlarmEntity>> {
        return try {
            alarmDao.getAllAlarms()
        }catch (e : Exception){
            return flowOf()
        }
    }
    override suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity) : Int{
        return try {
            alarmDao.deleteAlarmFromDataBase(alarmEntity)
        }catch (e : Exception){
            -1
        }
    }

    override suspend fun insertHomeScreen(homeEntity: HomeEntity): Long {
        return try {
            homeDao.insertHomeScreen(homeEntity)
        }catch (e : Exception){
            0
        }
    }

    override fun getHomeObj(): Flow<HomeEntity> {
        return try {
            homeDao.getHomeObj()
        }catch (e : Exception){
           flowOf()
        }
    }

    companion object {
        private var INSTANCE: GustyLocalDataSourceImpl? = null
        fun getInstance(
            dao: FavoriteDao ,
            alarmDao: AlarmDao ,
            homeDao: HomeDao
        ): GustyLocalDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val temp = GustyLocalDataSourceImpl(dao , alarmDao ,homeDao )
                INSTANCE = temp
                temp
            }
        }
    }
}