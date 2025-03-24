package com.example.gusty.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gusty.R
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.dark_blue

data class HourlyWeather(
    val temperature: Int,
    val weatherIcon: Int,
    val time: String
)

// Dummy list of hourly weather data
val dummyHourlyWeatherData = listOf(
    HourlyWeather(14, R.drawable.sun_image, "06:00 AM"),
    HourlyWeather(16, R.drawable.sun_image, "07:00 AM"),
    HourlyWeather(18, R.drawable.sun_image, "08:00 AM"),
    HourlyWeather(20, R.drawable.sun_image, "09:00 AM"),
    HourlyWeather(22, R.drawable.sun_image, "10:00 AM"),
    HourlyWeather(24, R.drawable.sun_image, "11:00 AM")
)

data class MeteorologicalParam(
    val temperature: Float,
    val metoIcon: Int, // Drawable resource ID
    val metoName: String
)

// Dummy list of hourly weather data
val dummyMeteorologicalParam = listOf(
    MeteorologicalParam(4.09f, R.drawable.wind_icon, "Wind"),
    MeteorologicalParam(2.73f, R.drawable.rain_icon, "Rain"),
    MeteorologicalParam(83f, R.drawable.cloudy_icon, "Cloud"),
    MeteorologicalParam(20f, R.drawable.pressure_icon, "Pressure"),
)

@Composable
fun HomeScreen() {
    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible.value = true
    }
    Column(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        blue, dark_blue
                    )
                )
            )
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(10.dp))
        DailyWeatherInfoCard()
        Spacer(Modifier.height(10.dp))
        DailyAndHourlyWeatherCard()
        Spacer(Modifier.height(10.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(dummyMeteorologicalParam) { _, meto ->
                MeteorologicalParameters(meto)
            }
        }

    }
}
@Composable
fun DailyWeatherInfoCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(top = 10.dp, start = 5.dp)) {
            Row {
                Text(
                    "Egypt", fontSize = 15.sp, modifier = Modifier
                        .padding(top = 15.dp), color = Color.White
                )

                Text(
                    " , Cairo", fontSize = 15.sp, modifier = Modifier
                        .padding(top = 15.dp), color = Color.White
                )
                Image(
                    painter = painterResource(R.drawable.location),
                    contentDescription = "location",
                    modifier =
                    Modifier
                        .padding(start = 3.dp, top = 15.dp)
                        .size(width = 17.dp, height = 17.dp)
                )
            }
            Row {
                Text(
                    "14",
                    fontSize = 70.sp,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "C", modifier = Modifier.padding(5.dp), color = Color.White
                )
            }
            Text(
                text = "Mostly Cloudy",
                modifier =
                Modifier.padding(top = 10.dp, start = 5.dp),
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(5.dp))
            Column {
                Row {
                    Text(
                        " 18",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp, start = 10.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "C", modifier = Modifier.padding(5.dp), color = Color.White
                    )
                    Text(
                        "/", fontSize = 15.sp, modifier = Modifier
                            .padding(top = 15.dp), color = Color.White
                    )
                    Text(
                        " 12",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "C", modifier = Modifier.padding(5.dp), color = Color.White
                    )
                    Text(
                        text = "Feels Like",
                        color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 5.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        " 12",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold

                    )

                    Text(
                        text = "C", modifier = Modifier.padding(5.dp), color = Color.White
                    )
                }

            }
        }

    }
}
@Composable
fun DailyAndHourlyWeatherCard() {
    val selectOption = remember { mutableStateOf("Hourly") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                Button(
                    onClick = { selectOption.value = "Hourly" },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectOption.value == "Hourly") Color.Blue else Color.Gray,
                        contentColor = Color.White // Text color
                    )
                ) {
                    Text(
                        text = "Hourly",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = { selectOption.value = "Daily" },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectOption.value == "Daily") Color.Black else Color.Gray,
                        contentColor = Color.White // Text color
                    )
                ) {
                    Text(
                        text = "Daily",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IsHourlyOrDaily(selectOption)
        }
    }
}
@Composable
fun MeteorologicalParameters(meteorologicalParam: MeteorologicalParam) {
    Card(
        modifier = Modifier
            .size(width = 140.dp, height = 150.dp)
            .padding(start = 20.dp, end = 20.dp, top = 15.dp)
            .shadow(
                elevation = 25.dp,
                ambientColor = Color.Black,
                spotColor = Color.Cyan,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Gray.copy(0.0f)
                )
        ) {

            Image(
                painter = painterResource(meteorologicalParam.metoIcon),
                contentDescription = "icon",
                modifier = Modifier
                    .size(width = 100.dp, height = 50.dp)
                    .padding(top = 5.dp)
            )
            Text(
                text = "${meteorologicalParam.temperature} + m/s",
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = meteorologicalParam.metoName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun IsHourlyOrDaily(selectOption: MutableState<String>) {
    if(selectOption.value == "Hourly"){
        LazyRow {
            itemsIndexed(dummyHourlyWeatherData){_,hourly->
                HourlyWeatherItem(hourly)
            }
        }
    }else {
        LazyRow {
            itemsIndexed(dummyHourlyWeatherData){_,hourly->
                HourlyWeatherItem(hourly)
            }
        }
    }
}
@Composable
fun HourlyWeatherItem(hourlyWeather: HourlyWeather) {
    Column(
        modifier = Modifier
            .padding(start = 3.dp , end = 3.dp)
    ) {
        Card(
            modifier = Modifier
                .size(width = 140.dp, height = 200.dp)
                .padding(start = 20.dp, end = 20.dp, top = 15.dp)
                .shadow(
                    elevation = 25.dp,
                    ambientColor = Color.Black,
                    spotColor = Color.Cyan,
                    shape = RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                blue, dark_blue
                            )
                        )
                    )
            ) {
                Text(
                    text = "C",
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 50.dp)
                )
                Text(
                    hourlyWeather.temperature.toString(),
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(hourlyWeather.weatherIcon),
                    contentDescription = "weather image",
                    Modifier
                        .size(width = 80.dp, height = 80.dp)
                        .padding(start = 20.dp)
                )
                Text(
                    text = hourlyWeather.time,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold
                )

            }
        }
    }
}


