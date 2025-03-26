package com.example.gusty.data.repo

import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import kotlinx.coroutines.flow.Flow

interface GustyRepo {

    suspend fun getCurrentWeather() : Flow<CurrentWeatherDto>
    suspend fun getDailyAndHourlyWeather() : Flow<HourlyAndDailyDto>
}