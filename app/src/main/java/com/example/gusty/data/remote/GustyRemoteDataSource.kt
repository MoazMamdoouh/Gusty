package com.example.gusty.data.remote

import android.util.Log
import com.example.gusty.data.local.GustyLocalDataSource
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.data.repo.GustyRepoImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyRemoteDataSource private constructor(private val api : Api) {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double , unit : String): Flow<CurrentWeatherDto> {
       return try {
           val response = api.getDailyWeatherInfo(latitude , longitude , unit =  unit)
           flowOf(response)
       }catch (e : Exception){
           flowOf()
       }
    }

    suspend fun getHourlyAndDailyWeather(lat : Double , lon : Double , unit: String) : Flow<HourlyAndDailyDto>{
        return try {
            val hourlyAndDailyResponse = api.getHourlyAndDailyWeather(lat , lon ,unit =  unit)
            flowOf(hourlyAndDailyResponse)
        }catch (e : Exception){
            flowOf()
        }
    }
    companion object {
        private var INSTANCE: GustyRemoteDataSource? = null
        fun getInstance(
            api: Api
        ): GustyRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                val temp = GustyRemoteDataSource(api)
                INSTANCE = temp
                temp
            }
        }
    }
}