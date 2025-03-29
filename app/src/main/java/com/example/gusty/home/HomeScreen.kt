package com.example.gusty.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gusty.R
import com.example.gusty.home.model.CurrentWeatherModel
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel
import com.example.gusty.ui.theme.nightColor
import com.example.gusty.utilities.LocationPermission

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val currentWeatherViewModel = homeViewModel.currentWeather.observeAsState()
    val hourlyWeatherViewModel = homeViewModel.hourlyWeather.observeAsState()
    val dailyWeatherViewModel = homeViewModel.dailyWeather.observeAsState()
    homeViewModel.getCurrentWeather(LocationPermission.locationState.value.latitude
        , LocationPermission.locationState.value.longitude)
    homeViewModel.getHourlyWeather()
    homeViewModel.getDailyWeather()
    Column(
        modifier = Modifier
            .background(currentWeatherViewModel.value?.backgroundColor ?: Color.White )
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(10.dp))
        DailyWeatherInfoCard(currentWeatherViewModel)
        Spacer(Modifier.height(10.dp))
        DailyAndHourlyWeatherCard(hourlyWeatherViewModel , dailyWeatherViewModel)
        Spacer(Modifier.height(10.dp))
        Row {
            WindCard(currentWeatherViewModel)
            RainCard(currentWeatherViewModel)
        }
        Spacer(Modifier.height(5.dp))
        Row {
            CloudCard(currentWeatherViewModel)
            PressureCard(currentWeatherViewModel)
        }
    }
}
@Composable
fun DailyWeatherInfoCard(currentWeatherViewModel: State<CurrentWeatherModel?>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(top = 10.dp, start = 5.dp)) {
            Row {
                currentWeatherViewModel.value?.let {
                    Text(
                        it.countryName, fontSize = 20.sp, modifier = Modifier
                            .padding(top = 15.dp), color = Color.White, fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    " , ${currentWeatherViewModel.value?.cityName}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 15.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
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
                    "${currentWeatherViewModel.value?.main?.temperature}",
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
            currentWeatherViewModel.value?.weather?.get(0)?.description?.let {
                Text(
                    text = it,
                    modifier =
                    Modifier.padding(top = 10.dp, start = 5.dp),
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(5.dp))
            Column {
                Row {
                    Text(
                        "${currentWeatherViewModel.value?.main?.minimumTemperature}",
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
                        " / ", fontSize = 15.sp, modifier = Modifier
                            .padding(top = 15.dp), color = Color.White
                    )
                    Text(
                        "${currentWeatherViewModel.value?.main?.maximumTemperature}",
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
                        "${currentWeatherViewModel.value?.main?.feelsLike}",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp, start = 5.dp),
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
fun DailyAndHourlyWeatherCard(
    hourlyWeatheViewModel: State<List<HourlyAndDailyModel>?>,
    dailyWeatherViewModel: State<List<HourlyAndDailyModel>?>
) {
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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Button(
                    onClick = { selectOption.value = "Hourly" },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectOption.value == "Hourly") Color.Blue else Color.Gray,
                        contentColor = Color.White
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
                        containerColor = if (selectOption.value == "Daily") Color.Black else nightColor,
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
            IsHourlyOrDaily(selectOption , hourlyWeatheViewModel , dailyWeatherViewModel )
        }
    }
}
@Composable
fun WindCard(currentWeatherViewModel: State<CurrentWeatherModel?>) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wind_icon), // Replace with your drawable
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White) // Optional tint
                )
                Text(
                    text = "Wind",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 5.dp)) {
                Text(
                    text = "Speed : ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.value?.wind?.speed} km/h",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 5.dp)) {
                Text(
                    text = "deg : ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${currentWeatherViewModel.value?.wind?.deg}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "o", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }

            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 5.dp)) {
                Text(
                    text = "gust  : ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.value?.wind?.gust} m/s",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun RainCard(currentWeatherViewModel: State<CurrentWeatherModel?>) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rain_icon), // Replace with your drawable
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White) // Optional tint
                )
                Text(
                    text = "Rain",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 5.dp)) {
                Text(
                    text = "Last Hour : ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.value?.rain} km/h",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun CloudCard(currentWeatherViewModel: State<CurrentWeatherModel?>) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cloudy_icon),
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "Clouds",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 5.dp)) {
                Text(
                    text = "Clouds : ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.value?.clouds?.all} %",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun PressureCard(currentWeatherViewModel: State<CurrentWeatherModel?>) {
    Card(
        modifier = Modifier
            .width(190.dp)
            .padding(8.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pressure_icon), // Replace with your drawable
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White) // Optional tint
                )
                Text(
                    text = "Pressure",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 5.dp)) {
                Text(
                    text = "Pressure : ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.value?.main?.pressure} hPa",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun IsHourlyOrDaily(
    selectOption: MutableState<String>,
    hourlyWeatherViewModel: State<List<HourlyAndDailyModel>?>,
    dailyWeatherViewModel: State<List<HourlyAndDailyModel>?>
) {


    if (selectOption.value == "Hourly") {
        LazyRow {
            itemsIndexed(hourlyWeatherViewModel.value.orEmpty()){ _, hour ->
                HourlyWeatherItem(hour)
            }
        }
    } else {
        LazyRow {
            itemsIndexed(dailyWeatherViewModel.value.orEmpty()) { _, daily ->
                DailyWeatherItem(daily)
            }
        }
    }
}
@Composable
fun HourlyWeatherItem(hour: HourlyAndDailyModel) {
    Column(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp)
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
                       hour.backGroundColor
                    )
            ) {
                Image(
                    painter = painterResource(hour.icon)
                    , contentDescription = "weather_icon"
                    , modifier = Modifier.fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 5.dp)
                )
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
                   "${hour.temperature}",
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = hour.time,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DailyWeatherItem(daily: HourlyAndDailyModel) {
    Column(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp)
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
                        daily.backGroundColor
                    )
            ) {
                Image(
                    painter = painterResource(daily.icon)
                    , contentDescription = "weather_icon"
                    , modifier = Modifier.fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 5.dp)
                )
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
                    "${daily.temperature}",
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

