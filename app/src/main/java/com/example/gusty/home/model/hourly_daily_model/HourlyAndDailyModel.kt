package com.example.gusty.home.model.hourly_daily_model

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.gusty.R
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.setting.LanguagePreference
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.nightColor
import com.example.gusty.ui.theme.orange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class HourlyAndDailyModel(
    val temperature: Int,
    val time: String,
    val icon: Int,
    val backGroundColor: Color,
    val day: String
)

fun HourlyAndDailyDto.hourlyModel(): List<HourlyAndDailyModel> {
    val timeZone = city.timezone
    val groupedForecast = list.groupBy { it.dt_txt?.substring(0, 10) }
    val todayKey = groupedForecast.keys.firstOrNull()
    val todayForecast = groupedForecast[todayKey] ?: emptyList()
    val upcomingDays = groupedForecast.filterKeys { it != todayKey }
    val currentDay = getCurrentDay(timeZone)

    return todayForecast.map {
        val adjustTimeStamp = it.dt + timeZone
        val hour = getHourFromAdjustedDt(adjustTimeStamp)
        val timeInString = convertUnixToHour(adjustTimeStamp)
        val backGround = getBackGroundColor(hour)
        val newIcon = convertIcon(it.weather.firstOrNull()?.icon)
        HourlyAndDailyModel(
            temperature = it.main.temp.toInt(),
            time = timeInString,
            icon = newIcon,
            backGroundColor = backGround,
            day = ""
        )
    }

}

fun HourlyAndDailyDto.mapDailyDtoToModel(): List<HourlyAndDailyModel> {

    return list.filter {
        val formattedTime = convertUnixToHour(it.dt)
        formattedTime.equals("11 AM", ignoreCase = true) ||
                formattedTime.equals("١١ ص", ignoreCase = true)
    }.map {
        Log.i("daily", "mapDailyDtoToModel: ${it.dt}")
        val day = convertUnixToDayOfWeek(dt = it.dt)
        val timeInString = convertUnixToHour(it.dt)
        val timeInInt = convertHourToInt(timeInString)
        val backGround = getBackGroundColor(timeInInt)
        val newIcon = convertIcon(it.weather.firstOrNull()?.icon)
        HourlyAndDailyModel(
            temperature = it.main.temp.toInt(),
            time = timeInString,
            icon = newIcon,
            backGroundColor = backGround,
            day = day
        )
    }
}

fun getCurrentDay(timeZone: Int): String {
    val currentUtc = System.currentTimeMillis() / 1000
    val adjustedTime = currentUtc + timeZone
    return convertUnixToDate(adjustedTime)
}

private fun getHourFromAdjustedDt(adjustedDt: Long): Int {
    val date = Date(adjustedDt * 1000L)
    val calendar = Calendar.getInstance().apply { time = date }
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun convertUnixToDate(dt: Long): String {
    val date = Date(dt * 1000L)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
}

fun convertUnixToHour(dt: Long): String {
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
        "03d" -> R.drawable.cloudy_sky_new
        "03n" -> R.drawable.cloudy_sky_new
        "04d" -> R.drawable.cloudy_sky_new
        "04n" -> R.drawable.cloudy_sky_new
        "09d" -> R.drawable.rain_new
        "09n" -> R.drawable.rain_new
        "010d" -> R.drawable.rain_new
        "010n" -> R.drawable.rain_new
        "11d" -> R.drawable.rain_with_thunder
        "11n" -> R.drawable.rain_with_thunder
        "13d" -> R.drawable.snow_sky
        "13n" -> R.drawable.snow_sky
        else -> R.drawable.windy_new
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
        in 14..17 -> orange
        in 18..19 -> Color.Red
        in 20..23, in 0..5 -> nightColor
        else -> Color.Black
    }
}

fun convertUnixToDayOfWeek(dt: Long): String {
    val date = Date(dt * 1000L)
    val format = SimpleDateFormat("EEEE", Locale.getDefault())
    return format.format(date)
}