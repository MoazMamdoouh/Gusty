package com.example.gusty.home

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gusty.R
import com.example.gusty.home.model.CurrentWeatherModel
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel
import com.example.gusty.setting.LanguagePreference
import com.example.gusty.setting.Preference
import com.example.gusty.setting.UnitPreference
import com.example.gusty.setting.WindPreference
import com.example.gusty.utilities.BackGrounds
import com.example.gusty.utilities.UiStateResult

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, lat: Double = 0.0, lon: Double = 0.0) {
    val currentWeatherViewModel = homeViewModel.currentWeather.collectAsStateWithLifecycle().value
    val hourlyWeatherViewModel = homeViewModel.hourlyWeather.observeAsState()
    val dailyWeatherViewModel = homeViewModel.dailyWeather.observeAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val finalLat = if (lat == 0.0) Preference.getLatitudeSharedPreference(context) else lat
    val finalLon = if (lon == 0.0) Preference.getLongitudeSharedPreference(context) else lon

    var background by remember { mutableStateOf(Color.White) }
    var secondBackground by remember { mutableStateOf(Color.White) }

    LaunchedEffect(currentWeatherViewModel) {
        if (currentWeatherViewModel is UiStateResult.Success) {
            background = currentWeatherViewModel.response.backgroundColor
            secondBackground = currentWeatherViewModel.response.secondBackGroundColor
        }
    }
    LaunchedEffect(finalLat, finalLon) {
        homeViewModel.getCurrentWeather(
            finalLat, finalLon,
            UnitPreference.getUnitSharedPreference(context) ?: "metric" ,
            LanguagePreference.getLanguagePref(context) ?: "en"
        )
        homeViewModel.getHourlyWeather(
            finalLat, finalLon,
            UnitPreference.getUnitSharedPreference(context) ?: "metric"
        )
        homeViewModel.getDailyWeather(
            finalLat, finalLon,
            UnitPreference.getUnitSharedPreference(context) ?: "metric"
        )
    }

    val unitAsChar = remember { mutableStateOf("") }
    val lang = LanguagePreference.getLanguagePref(context)
    when (UnitPreference.getUnitSharedPreference(context) ) {
        "metric" -> if(lang == "en")unitAsChar.value = "C" else unitAsChar.value =  "س"
        "imperial" -> if(lang == "en")unitAsChar.value = "F" else unitAsChar.value =  "ف"
        "standard" ->if(lang == "en")unitAsChar.value = "K" else unitAsChar.value =  "ك"
    }

    when (currentWeatherViewModel) {
        is UiStateResult.Failure -> {

            Log.i("home", "HomeScreen failure in UI state ")
        }
        is UiStateResult.Loading -> Log.i("home", "HomeScreen Loading in UI state ")
        is UiStateResult.Success -> {
            BackGrounds.setFirstBackGround(background)
            BackGrounds.setSecondBackGround(secondBackground)
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                background,
                                secondBackground
                            )
                        )
                    )
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(10.dp))
                DailyWeatherInfoCard(currentWeatherViewModel.response , unitAsChar)
                Spacer(Modifier.height(10.dp))
                DailyAndHourlyWeatherCard(
                    hourlyWeatherViewModel,
                    dailyWeatherViewModel,
                    currentWeatherViewModel.response.backgroundColor ,
                    unitAsChar
                )
                Spacer(Modifier.height(10.dp))
                Row {
                    WindCard(currentWeatherViewModel.response)
                    RainCard(currentWeatherViewModel.response)
                }
                Spacer(Modifier.height(5.dp))
                Row {
                    CloudCard(currentWeatherViewModel.response)
                    PressureCard(currentWeatherViewModel.response)
                }
            }
        }
    }

}

@Composable
fun DailyWeatherInfoCard(
    currentWeatherViewModel: CurrentWeatherModel,
    unitAsChar: MutableState<String>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(top = 10.dp, start = 5.dp)) {
            Row {
                currentWeatherViewModel.let {
                    Text(
                        it.countryName, fontSize = 20.sp, modifier = Modifier
                            .padding(top = 15.dp), color = Color.White, fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    " , ${currentWeatherViewModel.cityName}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 15.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(R.drawable.location),
                    contentDescription = "Location",
                    modifier =
                    Modifier
                        .padding(start = 3.dp, top = 15.dp)
                        .size(width = 17.dp, height = 17.dp)
                )
            }
            Row {
                Text(
                    "${currentWeatherViewModel.main.temperature}",
                    fontSize = 70.sp,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = unitAsChar.value, modifier = Modifier.padding(5.dp), color = Color.White , fontSize = 30.sp
                )
            }
            currentWeatherViewModel.weather[0].description.let {
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
                        "${currentWeatherViewModel.main.minimumTemperature}",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp, start = 10.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = unitAsChar.value,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White
                        , fontSize = 15.sp
                    )
                    Text(
                        " / ", fontSize = 15.sp, modifier = Modifier
                            .padding(top = 15.dp), color = Color.White
                    )
                    Text(
                        "${currentWeatherViewModel.main.maximumTemperature}",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = unitAsChar.value,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White
                        , fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.feels_like),
                        color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 5.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${currentWeatherViewModel.main.feelsLike}",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 15.dp, start = 5.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold

                    )

                    Text(
                        text = unitAsChar.value,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White ,
                        fontSize = 20.sp
                    )
                }

            }
        }

    }
}

@Composable
fun DailyAndHourlyWeatherCard(
    hourlyWeatheViewModel: State<List<HourlyAndDailyModel>?>,
    dailyWeatherViewModel: State<List<HourlyAndDailyModel>?>,
    backgroundColor: Color,
    unitAsChar: MutableState<String>
) {
    val context = LocalContext.current
    val selectOption = remember { mutableStateOf(context.getString(R.string.hourly)) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Button(
                    onClick = { selectOption.value = context.getString(R.string.hourly) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectOption.value == context.getString(R.string.hourly)) Color.Black else backgroundColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = context.getString(R.string.hourly),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = { selectOption.value = context.getString(R.string.daily) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectOption.value == context.getString(R.string.daily)) Color.Black else backgroundColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = context.getString(R.string.daily),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IsHourlyOrDaily(selectOption, hourlyWeatheViewModel, dailyWeatherViewModel , unitAsChar)
        }
    }
}

@Composable
fun WindCard(currentWeatherViewModel: CurrentWeatherModel) {
    val context = LocalContext.current
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
                    painter = painterResource(id = R.drawable.wind_icon),
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = stringResource(R.string.wind),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.speed),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.wind.speed} " +
                            WindPreference.getWindSharedPreference(context),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.deg),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${currentWeatherViewModel.wind.deg}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.o),
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.gust),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.wind.gust}  ${stringResource(R.string.m_s)}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RainCard(currentWeatherViewModel: CurrentWeatherModel) {
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
                    painter = painterResource(id = R.drawable.rain_icon),
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = stringResource(R.string.rain),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.last_hour),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(

                    text = " ${currentWeatherViewModel.rain} ${stringResource(R.string.km_h)} ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CloudCard(currentWeatherViewModel: CurrentWeatherModel) {
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
                    text = stringResource(R.string.clouds),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.clouds),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${currentWeatherViewModel.clouds.all} %",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PressureCard(currentWeatherViewModel: CurrentWeatherModel) {
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
                    painter = painterResource(id = R.drawable.pressure_icon),
                    contentDescription = "Wind Icon",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = stringResource(R.string.pressure),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.pressure_),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(

                    text = " ${currentWeatherViewModel.main.pressure} ${stringResource(R.string.hpa)} ",
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
    dailyWeatherViewModel: State<List<HourlyAndDailyModel>?>,
    unitAsChar: MutableState<String>
) {


    if (selectOption.value == stringResource(R.string.hourly)) {
        LazyRow {
            itemsIndexed(hourlyWeatherViewModel.value.orEmpty()) { _, hour ->
                HourlyWeatherItem(hour , unitAsChar)
            }
        }
    } else {
        LazyRow {
            itemsIndexed(dailyWeatherViewModel.value.orEmpty()) { _, daily ->
                DailyWeatherItem(daily , unitAsChar)
            }
        }
    }
}

@Composable
fun HourlyWeatherItem(hour: HourlyAndDailyModel, unitAsChar: MutableState<String>) {
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
                    painter = painterResource(hour.icon),
                    contentDescription = "weather_icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 5.dp)
                )
                Text(
                    text = unitAsChar.value,
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
fun DailyWeatherItem(daily: HourlyAndDailyModel, unitAsChar: MutableState<String>) {
    Column(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .size(width = 140.dp, height = 200.dp)
                .padding(start = 20.dp, end = 20.dp, top = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = daily.day,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Image(
                    painter = painterResource(daily.icon),
                    contentDescription = "weather_icon",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .padding(top = 5.dp)
                )
                Text(
                    text =unitAsChar.value,
                    color = Color.Black,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 50.dp)
                )
                Text(
                    "${daily.temperature}",
                    color = Color.Black,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
