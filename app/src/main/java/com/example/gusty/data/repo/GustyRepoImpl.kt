package com.example.gusty.data.repo

import android.util.Log
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.data.remote.GustyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class GustyRepoImpl private constructor(
    private val weatherRemoteDataSource: GustyRemoteDataSource
) : GustyRepo{
    override suspend fun getCurrentWeather(): Flow<CurrentWeatherDto>{
        return try {
            Log.i("TAG", "getCurrentWeather: repo Success")
             weatherRemoteDataSource.getCurrentWeather()
        }catch (e : Exception){
            Log.i("TAG", "getCurrentWeather: repo  error ${e.message} ")
            flowOf()
        }
    }

    override suspend fun getDailyAndHourlyWeather(): Flow<HourlyAndDailyDto> {
        return try {
            Log.i("TAG", "getDailyAndHourlyWeather repo : success with  ")
            weatherRemoteDataSource.getHourlyAndDailyWeather()
        }catch (e : Exception){
            Log.i("TAG", "getDailyAndHourlyWeather repo : error ")
            flowOf()
        }
    }


    companion object {
        private var INSTANCE : GustyRepoImpl? = null
        fun getInstance(gustyRemoteDataSource: GustyRemoteDataSource) : GustyRepoImpl{
            return INSTANCE ?: synchronized(this){
                val temp = GustyRepoImpl(gustyRemoteDataSource)
                INSTANCE = temp
                temp
            }
        }
    }
}