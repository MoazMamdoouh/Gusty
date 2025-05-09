package com.example.gusty.data.repo

import android.util.Log
import com.example.gusty.data.local.GustyLocalDataSource
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.HomeEntity
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.data.remote.GustyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class GustyRepoImpl private constructor(
    private val weatherRemoteDataSource: GustyRemoteDataSource,
    private val gustyLocalDataSource: GustyLocalDataSource
) : GustyRepo {
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double ,
        unit : String ,
        lang : String
    ): Flow<CurrentWeatherDto> {
        return try {
            weatherRemoteDataSource.getCurrentWeather(latitude, longitude , unit , lang )
        } catch (e: Exception) {
            Log.i("TAG", "getCurrentWeather: repo  error ${e.message} ")
            flowOf()
        }
    }

    override suspend fun getDailyAndHourlyWeather(latitude: Double, longitude: Double , unit: String): Flow<HourlyAndDailyDto> {
        return try {
            weatherRemoteDataSource.getHourlyAndDailyWeather(latitude , longitude , unit)
        } catch (e: Exception) {
            Log.i("TAG", "getDailyAndHourlyWeather repo : error ")
            flowOf()
        }
    }

    override suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long {
        return try {
            gustyLocalDataSource.insertItemToFavorite(favoriteEntity)
        } catch (e: Exception) {
            Log.i("TAG", "insertItemToFavorite: error ${e.message}")
            return 0
        }
    }

    override fun getListOfFavoriteItems(): Flow<List<FavoriteEntity>> {
        return try {
            gustyLocalDataSource.getListOfFavoriteItems()
        }catch ( e :Exception){
            return flowOf()
        }
    }

    override suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity): Int {
        return try {
            gustyLocalDataSource.deleteLocationFromFavorite(favoriteEntity)
        }catch (e : Exception){
            0
        }
    }

    override suspend fun insertAlarmToDataBase(alarmEntity: AlarmEntity): Long {
        return try {
            gustyLocalDataSource.insertAlarmToDataBase(alarmEntity)
        }catch (e : Exception){
            Log.i("TAG", "insertAlarmToDataBase repo  error ${e.message}")
                -1
        }
    }

    override fun getAllAlarmsFromDataBase(): Flow<List<AlarmEntity>> {
        return try {
            gustyLocalDataSource.getAllAlarmsFromDataBase()
        }catch (e : Exception){
            flowOf()
        }
    }

    override suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity): Int {
        return try {
            gustyLocalDataSource.deleteAlarmFromDataBase(alarmEntity)
        }catch (e : Exception){
            -1
        }
    }

    override suspend fun insertHomeScreen(homeEntity: HomeEntity): Long {
        return try {
           gustyLocalDataSource.insertHomeScreen(homeEntity)
        }catch (e : Exception){
            0
        }
    }

    override fun getHomeObj(): Flow<HomeEntity> {
        return try {
            gustyLocalDataSource.getHomeObj()
        }catch (e : Exception){
            flowOf()
        }
    }


    companion object {
        private var INSTANCE: GustyRepoImpl? = null
        fun getInstance(
            gustyRemoteDataSourceImpl: GustyRemoteDataSource, gustyLocalDataSourceImpl: GustyLocalDataSource
        ): GustyRepoImpl {
            return INSTANCE ?: synchronized(this) {
                val temp = GustyRepoImpl(gustyRemoteDataSourceImpl, gustyLocalDataSourceImpl)
                INSTANCE = temp
                temp
            }
        }
    }
}