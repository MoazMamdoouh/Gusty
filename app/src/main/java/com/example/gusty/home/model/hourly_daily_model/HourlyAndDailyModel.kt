package com.example.gusty.home.model.hourly_daily_model

import androidx.compose.ui.graphics.Color
import com.example.gusty.R
import com.example.gusty.data.model.hourly_daily_dto.HourlyAndDailyDto
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.nightColor
import com.example.gusty.ui.theme.orange
import com.example.gusty.ui.theme.red
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
    // Group entries by their local date
    val groupedForecast = list.groupBy { entry ->
        val localDt = entry.dt + timeZone
        convertUnixToDate(localDt)
    }
    val currentDate = getCurrentDay(timeZone)
    val todayForecast = groupedForecast[currentDate] ?: emptyList()
    val currentLocalTime = (System.currentTimeMillis() / 1000) + timeZone

    // Filter to include only future hours
    val filteredTodayForecast = todayForecast.filter { entry ->
        (entry.dt + timeZone) >= currentLocalTime
    }

    return filteredTodayForecast.map { entry ->
        val adjustTimeStamp = entry.dt + timeZone
        val hour = getHourFromAdjustedDt(adjustTimeStamp)
        val timeInString = convertUnixToHour(adjustTimeStamp)
        val backGround = getBackGroundColor(hour)
        val newIcon = convertIcon(entry.weather.firstOrNull()?.icon)
        HourlyAndDailyModel(
            temperature = entry.main.temp.toInt(),
            time = timeInString,
            icon = newIcon,
            backGroundColor = backGround,
            day = ""
        )
    }
}

fun HourlyAndDailyDto.mapDailyDtoToModel(): List<HourlyAndDailyModel> {
    val timeZone = city.timezone
    // Group entries by local date
    val groupedByDay = list.groupBy { entry ->
        val localDt = entry.dt + timeZone
        convertUnixToDate(localDt)
    }

    val currentDate = getCurrentDay(timeZone)

    // Process each day except today
    return groupedByDay.filterKeys { it != currentDate }.values.mapNotNull { dailyEntries ->
        // Select entry closest to noon (12 PM)
        dailyEntries.minByOrNull { entry ->
            val localHour = getHourFromAdjustedDt(entry.dt + timeZone)
            kotlin.math.abs(12 - localHour)
        }
    }.map { entry ->
        val localDt = entry.dt + timeZone
        val day = convertUnixToDayOfWeek(localDt)
        val timeInString = convertUnixToHour(localDt)
        val hour = getHourFromAdjustedDt(localDt)
        val backGround = getBackGroundColor(hour)
        val newIcon = convertIcon(entry.weather.firstOrNull()?.icon)
        HourlyAndDailyModel(
            temperature = entry.main.temp.toInt(),
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
        in 18..19 -> red
        in 20..23, in 0..5 -> nightColor
        else -> Color.Black
    }
}

fun convertUnixToDayOfWeek(dt: Long): String {
    val date = Date(dt * 1000L) // dt is already local timestamp
    val format = SimpleDateFormat("EEEE", Locale.getDefault())
    return format.format(date)
}

// Other functions remain the same but ensure they receive the correct local timestamps