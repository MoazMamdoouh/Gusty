package com.example.gusty.data.remote

import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyRemoteDataSourceImpl private constructor(private val api: Api) : GustyRemoteDataSource {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        unit: String ,
        lang : String
    ): Flow<CurrentWeatherDto> {
        return try {
            val response = api.getDailyWeatherInfo(latitude, longitude, unit = unit , lang = lang)
            flowOf(response)
        } catch (e: Exception) {
            flowOf()
        }
    }

    override suspend fun getHourlyAndDailyWeather(
        lat: Double,
        lon: Double,
        unit: String
    ): Flow<HourlyAndDailyDto> {
        return try {
            val hourlyAndDailyResponse = api.getHourlyAndDailyWeather(lat, lon, unit = unit)
            flowOf(hourlyAndDailyResponse)
        } catch (e: Exception) {
            flowOf()
        }
    }

    companion object {
        private var INSTANCE: GustyRemoteDataSourceImpl? = null
        fun getInstance(
            api: Api
        ): GustyRemoteDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val temp = GustyRemoteDataSourceImpl(api)
                INSTANCE = temp
                temp
            }
        }
    }
}