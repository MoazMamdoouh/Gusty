package com.example.gusty.home.model

import androidx.compose.ui.graphics.Color
import com.example.gusty.data.model.curren_weather_dto.Clouds
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.model.curren_weather_dto.Rain
import com.example.gusty.data.model.curren_weather_dto.Weather
import com.example.gusty.data.model.curren_weather_dto.Wind
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.dark_blue
import com.example.gusty.ui.theme.light_orange
import com.example.gusty.ui.theme.nightColor
import com.example.gusty.ui.theme.orange
import com.example.gusty.ui.theme.red
import com.example.gusty.ui.theme.second_red
import com.example.gusty.ui.theme.whiteNightColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class CurrentWeatherModel(
    val lat: Double,
    val lon: Double,
    val weather: List<Weather>,
    val main: MainModel,
    val countryName: String,
    val time: Int,
    val id: Int,
    val cityName: String,
    val wind: Wind,
    val rain: Rain?,
    val clouds: Clouds,
    val backgroundColor: Color,
    val secondBackGroundColor: Color
)

fun CurrentWeatherDto.mapDtoToModel(): CurrentWeatherModel {
    val adjustedDt = dt + timezone
    val countryName = getCountryNameFromCode(sys.country)
    val timeInHour = convertUnixToHour(adjustedDt.toInt())
    val timeAsInt = convertHourToInt(timeInHour)
    val backgroundColor = getBackGroundColor(timeAsInt)
    val secondBackGroundColor = getSecondBackGroundColor(backgroundColor)
    return CurrentWeatherModel(
        lat = coord.lat,
        lon = coord.lon,
        weather = weather,
        main = MainModel.fromDto(main),
        countryName = countryName,
        time = dt.toInt(),
        id = id,
        cityName = name,
        wind = wind,
        rain = rain,
        clouds = clouds,
        backgroundColor = backgroundColor,
        secondBackGroundColor = secondBackGroundColor
    )
}

fun convertUnixToHour(dt: Int): String {
    val date = Date(dt * 1000L)
    val format = SimpleDateFormat("h a", Locale.getDefault())
    return format.format(date)
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

fun getSecondBackGroundColor(color: Color): Color {
    return when (color) {
        blue -> dark_blue
        orange -> light_orange
        red -> second_red
        nightColor -> whiteNightColor
        else -> Color.Black
    }
}

fun getCountryNameFromCode(countryCode: String): String {
    val locale = Locale("", countryCode)
    return locale.displayCountry
}
