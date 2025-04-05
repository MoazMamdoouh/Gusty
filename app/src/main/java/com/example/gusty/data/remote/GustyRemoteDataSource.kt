package com.example.gusty.data.remote

import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GustyRemoteDataSource {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double , unit : String , lang : String): Flow<CurrentWeatherDto>
    suspend fun getHourlyAndDailyWeather(lat : Double , lon : Double , unit: String) : Flow<HourlyAndDailyDto>
}