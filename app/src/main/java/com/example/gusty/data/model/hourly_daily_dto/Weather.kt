package com.example.gusty.data.model.hourly_daily_dto

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)