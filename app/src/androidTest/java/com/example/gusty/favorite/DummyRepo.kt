package com.example.gusty.favorite

import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.data.repo.GustyRepo
import kotlinx.coroutines.flow.Flow

class DummyRepo() : GustyRepo {
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        unit: String
    ): Flow<CurrentWeatherDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getDailyAndHourlyWeather(
        latitude: Double,
        longitude: Double,
        unit: String
    ): Flow<HourlyAndDailyDto> {
        TODO("Not yet implemented")
    }

    override suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long {
       return 1
    }

    override fun getListOfFavoriteItems(): Flow<List<FavoriteEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity): Int {
        TODO("Not yet implemented")
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
}