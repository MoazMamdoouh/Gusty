package com.example.gusty.data.local.home

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.gusty.home.model.CurrentWeatherModel
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel


@Entity(tableName = "home_table")
data class HomeEntity(
    @PrimaryKey
    val id : Int = 1 ,
    @TypeConverters(CurrentWeatherTypeConvert::class)
    val currentWeather : CurrentWeatherModel,
    @TypeConverters(HourlyAndDailyTypeConvert::class)
    val hourlyAndDaily: List<HourlyAndDailyModel>
)