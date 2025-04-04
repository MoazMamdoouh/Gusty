package com.example.gusty.home.model.hourly_daily_model

import androidx.compose.ui.graphics.Color
import com.example.gusty.R
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.nightColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class HourlyAndDailyModel(
    val temperature: Int,
    val time: String,
    val icon: Int,
    val backGroundColor: Color
)

fun HourlyAndDailyDto.hourlyModel(): List<HourlyAndDailyModel> {
    return list.filter {
        convertUnixToDate(it.dt) == getCurrentDay()
    }.map {
        val timeInString = convertUnixToHour(it.dt)
        val timeInInt = convertHourToInt(timeInString)
        val backGround = getBackGroundColor(timeInInt)
        val newIcon = convertIcon(it.weather.firstOrNull()?.icon)
        HourlyAndDailyModel(
            temperature = it.main.temp.toInt(),
            time = timeInString,
            icon = newIcon,
            backGroundColor = backGround
        )
    }
}

fun HourlyAndDailyDto.mapDailyDtoToModel() : List<HourlyAndDailyModel>{
    return list.filter {
        val formattedTime = convertUnixToHour(it.dt)
        formattedTime.equals("11 AM", ignoreCase = true)
    }.map {
        val timeInString = convertUnixToHour(it.dt)
        val timeInInt = convertHourToInt(timeInString)
        val backGround = getBackGroundColor(timeInInt)
        val newIcon = convertIcon(it.weather.firstOrNull()?.icon)
        HourlyAndDailyModel(
            temperature = it.main.temp.toInt(),
            time = timeInString,
            icon = newIcon,
            backGroundColor = backGround
        )
    }
}

fun getCurrentDay(): String {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val current = formatter.format(time)
    return current
}

fun convertUnixToDate(dt: Int): String {
    val date = Date(dt * 1000L)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
}

fun convertUnixToHour(dt: Int): String {
    val date = Date(dt * 1000L)
    val format = SimpleDateFormat("h a", Locale.getDefault())
    return format.format(date)
}

fun convertIcon(icon: String?): Int {
    return when (icon) {
        "01d" -> R.drawable.clear_sky_morning
        "01n" -> R.drawable.clear_sky_night
        "02d" -> R.drawable.few_clouds_morning
        "02n" -> R.drawable.few_cloudy_night
        "03d" -> R.drawable.cloudy_icon
        "03n" -> R.drawable.cloudy_icon
        "04d" -> R.drawable.cloudy_icon
        "04n" -> R.drawable.cloudy_icon
        "09d" -> R.drawable.rain_icon
        "09n" -> R.drawable.rain_icon
        "010d" -> R.drawable.rain_icon
        "010n" -> R.drawable.rain_icon
        "11d" -> R.drawable.rain_with_thunder
        "11n" -> R.drawable.rain_with_thunder
        "13d" -> R.drawable.snow_sky
        "13n" -> R.drawable.snow_sky
        else -> R.drawable.wind_icon
    }
}


fun convertHourToInt(time: String): Int {
    val format = SimpleDateFormat("h a", Locale.getDefault())
    val date = format.parse(time) ?: return -1
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun getBackGroundColor(time: Int): Color {
    return when (time) {
        in 6..13 -> blue
        in 14..17 -> Color(0xFFFFA500)
        in 18..19 -> Color.Red
        in 20..23, in 0..5 -> nightColor
        else -> Color.Black
    }
}