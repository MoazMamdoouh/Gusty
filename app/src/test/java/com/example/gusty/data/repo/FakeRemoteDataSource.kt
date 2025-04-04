package com.example.gusty.data.repo

import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.data.remote.GustyRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSource : GustyRemoteDataSource {
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        unit: String
    ): Flow<CurrentWeatherDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getHourlyAndDailyWeather(
        lat: Double,
        lon: Double,
        unit: String
    ): Flow<HourlyAndDailyDto> {
        TODO("Not yet implemented")
    }
}