package com.example.gusty.data.repo

import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.HomeEntity
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import kotlinx.coroutines.flow.Flow

interface GustyRepo {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double , unit : String , lang : String ): Flow<CurrentWeatherDto>
    suspend fun getDailyAndHourlyWeather(latitude: Double, longitude: Double , unit: String) : Flow<HourlyAndDailyDto>
    suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity) : Long
    fun getListOfFavoriteItems() : Flow<List<FavoriteEntity>>
    suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity) : Int
    suspend fun insertAlarmToDataBase(alarmEntity: AlarmEntity) : Long
    fun getAllAlarmsFromDataBase() : Flow<List<AlarmEntity>>
    suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity) : Int
    suspend fun insertHomeScreen(homeEntity: HomeEntity) : Long
    fun getHomeObj() : Flow<HomeEntity>
}