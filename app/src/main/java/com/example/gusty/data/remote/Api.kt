package com.example.gusty.data.remote

import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    suspend fun getDailyWeatherInfo(
        @Query("lat") lat: Double =30.0529,
        @Query("lon") lon: Double = 31.3436,
        @Query("appid") apiKey: String = "b753d8e898af2c2c8235d35152cf5139"
    ) : CurrentWeatherDto

    @GET("forecast")
    suspend fun getHourlyAndDailyWeather(
        @Query("lat") lat: Double =30.0529,
        @Query("lon") lon: Double = 31.3436,
        @Query("appid") apiKey: String = "b753d8e898af2c2c8235d35152cf5139"
    ) : HourlyAndDailyDto
}