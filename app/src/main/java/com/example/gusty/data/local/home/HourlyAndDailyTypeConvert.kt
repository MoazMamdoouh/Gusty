package com.example.gusty.data.local.home

import androidx.room.TypeConverter
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyAndDailyTypeConvert {

    private val gson = Gson()

    @TypeConverter
    fun fromHourlyAndDailyModelList(value: List<HourlyAndDailyModel>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toHourlyAndDailyModelList(value: String): List<HourlyAndDailyModel> {
        val type = object : TypeToken<List<HourlyAndDailyModel>>() {}.type
        return gson.fromJson(value, type)
    }
}