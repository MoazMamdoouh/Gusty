package com.example.gusty.data.local.home

import androidx.room.TypeConverter
import com.example.gusty.home.model.CurrentWeatherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurrentWeatherTypeConvert {

    private val gson = Gson()
    @TypeConverter
    fun fromCurrentWeatherModel(value: CurrentWeatherModel): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurrentWeatherModel(value: String): CurrentWeatherModel {
        val type = object : TypeToken<CurrentWeatherModel>() {}.type
        return gson.fromJson(value, type)
    }

}
