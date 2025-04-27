package com.example.gusty.home.model.current_weather_model

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
                temperature = main.temp.toInt() ,
                feelsLike = main.feels_like.toInt(),
                minimumTemperature = main.temp_min.toInt(),
                maximumTemperature =main.temp_max.toInt() ,
                pressure = main.pressure
            )
        }
    }
}
