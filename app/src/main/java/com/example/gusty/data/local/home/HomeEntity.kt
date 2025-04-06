package com.example.gusty.data.local.home

import androidx.room.Entity
import com.example.gusty.home.model.CurrentWeatherModel
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel


@Entity(tableName = "home_table")
data class HomeEntity(
    val currentWeather: CurrentWeatherModel,
    val hourlyWeather: List<HourlyAndDailyModel>,
)