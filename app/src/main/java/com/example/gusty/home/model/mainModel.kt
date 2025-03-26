package com.example.gusty.home.model

import com.example.gusty.data.model.curren_weather_dto.Main

data class MainModel(
    val temperature: Int,
    val feelsLike: Int,
    val minimumTemperature: Int,
    val maximumTemperature: Int,
    val pressure: Int
) {
    companion object {
        fun fromDto(main: Main): MainModel {
            return MainModel(
                temperature = (main.temp - 273.15).toInt(),
                feelsLike = (main.feels_like - 273.15).toInt(),
                minimumTemperature = (main.temp_min - 273.15).toInt(),
                maximumTemperature =(main.temp_max - 273.15).toInt() ,
                pressure = main.pressure
            )
        }
    }
}
