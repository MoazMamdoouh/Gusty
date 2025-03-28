package com.example.gusty.data.remote

import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyRemoteDataSource(private val api : Api) {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Flow<CurrentWeatherDto> {
       return try {
           val response = api.getDailyWeatherInfo(latitude , longitude)
           flowOf(response)
       }catch (e : Exception){
           flowOf()
       }
    }

    suspend fun getHourlyAndDailyWeather() : Flow<HourlyAndDailyDto>{
        return try {
            val hourlyAndDailyResponse = api.getHourlyAndDailyWeather()
            flowOf(hourlyAndDailyResponse)
        }catch (e : Exception){
            flowOf()
        }
    }
}