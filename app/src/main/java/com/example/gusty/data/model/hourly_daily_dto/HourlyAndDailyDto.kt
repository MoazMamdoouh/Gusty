package com.example.gusty.data.model.hourly_daily_dto

data class HourlyAndDailyDto(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item0>,
    val message: Int
)