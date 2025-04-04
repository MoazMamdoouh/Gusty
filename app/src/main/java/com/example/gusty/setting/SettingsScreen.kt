package com.example.gusty.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gusty.R
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.dark_blue
import com.example.gusty.utilities.LocationPermission
import com.example.gusty.utilities.Routes


@Composable
fun SettingScreen(navController: NavHostController) {

    Column {
        LanguageCard()
        LocationCard(navController)
        UnitCard()
        WindSettingsCard()
    }
}

@Composable
fun LanguageCard() {
    var englishChecked by remember { mutableStateOf(true) }
    var arabicChecked by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Text(
            "Language",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(Modifier.height(5.dp))
        Row {
            Text(
                "English (Default) :  ",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 13.dp, start = 10.dp)
            )
            Switch(
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                checked = englishChecked,
                onCheckedChange = {
                    englishChecked = it
                }, thumbContent = if (englishChecked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = blue,
                    checkedTrackColor = dark_blue,
                    uncheckedThumbColor = dark_blue,
                    uncheckedTrackColor = blue
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Arabic :", color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 13.dp)
            )
            Switch(
                modifier = Modifier.padding(start = 5.dp),
                checked = arabicChecked,
                onCheckedChange = {
                    arabicChecked = it
                }, thumbContent = if (arabicChecked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = blue,
                    checkedTrackColor = dark_blue,
                    uncheckedThumbColor = dark_blue,
                    uncheckedTrackColor = blue
                )
            )
        }
    }
}

@Composable
fun LocationCard(navController: NavHostController) {
    var gpsChecked by remember { mutableStateOf(true) }
    var locationChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (Preference.getLocationStateSharedPreference(context).equals("gps")) {
        gpsChecked = true
    } else {
        gpsChecked = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Text(
            "Location",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(Modifier.height(5.dp))
        Row {
            Text(
                " GPS : ",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 13.dp, start = 10.dp)
            )
            Switch(
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                checked = gpsChecked,
                onCheckedChange = {
                    Preference.setLocationStateSharedPreference("gps", context)
                    gpsChecked = it
                }, thumbContent = if (gpsChecked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = blue,
                    checkedTrackColor = dark_blue,
                    uncheckedThumbColor = dark_blue,
                    uncheckedTrackColor = blue
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Location :", color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 13.dp)
            )
            Switch(
                modifier = Modifier.padding(start = 5.dp),
                checked = locationChecked,
                onCheckedChange = {
                    if (it) {
                        Preference.setLocationStateSharedPreference("location", context)
                    }
                    locationChecked = it
                },
                thumbContent = if (Preference.getLocationStateSharedPreference(context)
                        .equals("Location")
                ) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = blue,
                    checkedTrackColor = dark_blue,
                    uncheckedThumbColor = dark_blue,
                    uncheckedTrackColor = blue
                )
            )
            if (Preference.getLocationStateSharedPreference(context).equals("gps")) {
                Preference.setLatitudeSharedPreference(
                    LocationPermission.locationState.value.latitude,
                    context
                )
                Preference.setLongitudeSharedPreference(
                    LocationPermission.locationState.value.longitude,
                    context
                )
            }
            if (locationChecked) {
                navController.navigate(Routes.SETTINGS_MAP.toString())
                locationChecked = false
            }

        }
    }
}

@Composable
fun UnitCard() {
    //celsius and fahrenheit and kelvin
    val context = LocalContext.current
    val initUnit = when (UnitPreference.getUnitSharedPreference(context)) {
        "metric" -> UnitEnum.CELSIUS
        "imperial" -> UnitEnum.FAHRENHEIT
        "standard" -> UnitEnum.KELVIN
        else -> UnitEnum.KELVIN
    }
    var selectedUnit by remember { mutableStateOf<UnitEnum>(initUnit) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Text(
            "Unit",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(Modifier.height(5.dp))
        Row {
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {
                    UnitPreference.setUnitSharedPreference("metric", context)
                    WindPreference.setWindSharedPreference("m/s" , context)
                    selectedUnit = UnitEnum.CELSIUS
                },
                label = {
                    Text("Celsius")
                },
                selected = selectedUnit == UnitEnum.CELSIUS,
                leadingIcon = if (selectedUnit == UnitEnum.CELSIUS) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {
                    UnitPreference.setUnitSharedPreference("imperial", context)
                    WindPreference.setWindSharedPreference("mph" , context)
                    selectedUnit = UnitEnum.FAHRENHEIT
                },
                label = {
                    Text("Fahrenheit")
                },
                selected = selectedUnit == UnitEnum.FAHRENHEIT,
                leadingIcon = if (selectedUnit == UnitEnum.FAHRENHEIT) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {
                    UnitPreference.setUnitSharedPreference("standard", context)
                    WindPreference.setWindSharedPreference("m/s" , context)
                    selectedUnit = UnitEnum.KELVIN
                },
                label = {
                    Text("Kelvin")
                },
                selected = selectedUnit == UnitEnum.KELVIN,
                leadingIcon = if (selectedUnit == UnitEnum.KELVIN) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}


@Composable
fun WindSettingsCard() {

    val context = LocalContext.current
    val initUnit = when (WindPreference.getWindSharedPreference(context)) {
        "m/s" -> WindUnitEnum.MS
        "mph" -> WindUnitEnum.MPH
        else -> WindUnitEnum.MS
    }
    var selectedWindDegree by remember { mutableStateOf<WindUnitEnum>(initUnit) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Row {
            Image(painter = painterResource(R.drawable.wind_icon) , contentDescription = null
            ,Modifier.size(width = 50.dp , height = 50.dp)
                    .padding(start = 10.dp , top = 10.dp))
            Spacer(Modifier.width(5.dp))
            Text(
                context.getString(R.string.wind),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
            , horizontalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {},
                enabled = false,
                label = {
                    Text("M/S")
                },
                selected = selectedWindDegree == WindUnitEnum.MS,
                leadingIcon = if (selectedWindDegree == WindUnitEnum.MS) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {},
                enabled = false,
                label = {
                    Text("MPH")
                },
                selected = selectedWindDegree == WindUnitEnum.MPH,
                leadingIcon = if (selectedWindDegree == WindUnitEnum.MPH) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}
