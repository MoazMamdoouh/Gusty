package com.example.gusty.setting

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gusty.R
import com.example.gusty.utilities.BackGrounds
import com.example.gusty.utilities.LocationPermission
import com.example.gusty.utilities.Routes


@Composable
fun SettingScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        BackGrounds.getFirstBackGround(),
                        BackGrounds.getSecondBackGround()
                    )
                )
            )
            .fillMaxSize()
    ) {
        LanguageCard()
        LocationCard(navController)
        UnitCard()
        WindSettingsCard()
    }
}

@Composable
fun LanguageCard() {
    val context = LocalContext.current
    val initLocation = when (LanguagePreference.getLanguagePref(context)) {
        "en" ->LanguageEnum.ENGLISH
        "ar" -> LanguageEnum.ARABIC
        "def" -> LanguageEnum.DEFAULT
        else -> LanguageEnum.ENGLISH
    }
    var selectedArOrEn by remember { mutableStateOf(initLocation) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Column  {
            Spacer(Modifier.height(5.dp))
            Row {
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(R.string.language), color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 13.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {

                //default
                FilterChip(
                    onClick = {
                        LanguagePreference.setLanguagePref(context , systemLanguage(context))
                        selectedArOrEn = LanguageEnum.DEFAULT
                        restartApp(context)
                    },
                    label = {
                        Text(stringResource(R.string.defaultt))
                    },
                    selected = selectedArOrEn == LanguageEnum.DEFAULT,
                    leadingIcon = if (selectedArOrEn == LanguageEnum.DEFAULT) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }, colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Black,
                        selectedLeadingIconColor = Color.Black,
                        containerColor = Color.Gray,
                        labelColor = Color.White
                    )
                )
                //english
                Spacer(Modifier.width(5.dp))
                FilterChip(
                    onClick = {
                        LanguagePreference.setLanguagePref(context , "en")
                        selectedArOrEn = LanguageEnum.ENGLISH
                        restartApp(context)
                    },
                    label = {
                        Text(stringResource(R.string.english))
                    },
                    selected = selectedArOrEn == LanguageEnum.ENGLISH,
                    leadingIcon = if (selectedArOrEn == LanguageEnum.ENGLISH) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }, colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Black,
                        selectedLeadingIconColor = Color.Black,
                        containerColor = Color.Gray,
                        labelColor = Color.White
                    )
                )
                Spacer(Modifier.width(8.dp))
                //arabic
                FilterChip(
                    onClick = {
                        LanguagePreference.setLanguagePref(context , "ar")
                        selectedArOrEn = LanguageEnum.ARABIC
                        restartApp(context)
                    },
                    label = {
                        Text(stringResource(R.string.arabic))
                    },
                    selected = selectedArOrEn == LanguageEnum.ARABIC,
                    leadingIcon = if (selectedArOrEn == LanguageEnum.ARABIC) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }, colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Black,
                        selectedLeadingIconColor = Color.Black,
                        containerColor = Color.Gray,
                        labelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun LocationCard(navController: NavHostController) {
    val context = LocalContext.current
    val initLocation = when (Preference.getLocationStateSharedPreference(context)) {
        "gps" -> LocationEnum.GPS
        "location" -> LocationEnum.LOCATION
        else -> LocationEnum.GPS
    }
    var selectedLocationOrGps by remember { mutableStateOf(initLocation) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f)) // Light Blue Transparent
    ) {
        Row {
            Spacer(Modifier.height(5.dp))
            Row {
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(R.string.location), color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 13.dp)
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
        }
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
            Spacer(Modifier.width(8.dp))
            FilterChip(
                onClick = {
                    Preference.setLocationStateSharedPreference("gps", context)
                    Preference.setLatitudeSharedPreference(
                        LocationPermission.locationState.value.latitude,
                        context
                    )
                    Preference.setLongitudeSharedPreference(
                        LocationPermission.locationState.value.longitude,
                        context
                    )
                    selectedLocationOrGps = LocationEnum.GPS
                },
                label = {
                    Text(stringResource(R.string.gps))
                },
                selected = selectedLocationOrGps == LocationEnum.GPS,
                leadingIcon = if (selectedLocationOrGps == LocationEnum.GPS) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }, colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    selectedLeadingIconColor = Color.Black,
                    containerColor = Color.Gray,
                    labelColor = Color.White
                )
            )
            Spacer(Modifier.width(8.dp))
            FilterChip(
                onClick = {

                    Preference.setLocationStateSharedPreference("location", context)
                    navController.navigate(Routes.SETTINGS_MAP.toString())
                    selectedLocationOrGps = LocationEnum.LOCATION
                },
                label = {
                    Text(stringResource(R.string.location))
                },
                selected = selectedLocationOrGps == LocationEnum.LOCATION,
                leadingIcon = if (selectedLocationOrGps == LocationEnum.LOCATION) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }, colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    selectedLeadingIconColor = Color.Black,
                    containerColor = Color.Gray,
                    labelColor = Color.White
                )
            )
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
            stringResource(R.string.unit),
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
                    WindPreference.setWindSharedPreference("m/s", context)
                    selectedUnit = UnitEnum.CELSIUS
                },
                label = {
                    Text(stringResource(R.string.celsius))
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
                },colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    selectedLeadingIconColor = Color.Black,
                    containerColor = Color.Gray,
                    labelColor = Color.White
                )
            )
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {
                    UnitPreference.setUnitSharedPreference("imperial", context)
                    WindPreference.setWindSharedPreference("mph", context)
                    selectedUnit = UnitEnum.FAHRENHEIT
                },
                label = {
                    Text(stringResource(R.string.fahrenheit))
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
                },colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    selectedLeadingIconColor = Color.Black,
                    containerColor = Color.Gray,
                    labelColor = Color.White
                )
            )
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {
                    UnitPreference.setUnitSharedPreference("standard", context)
                    WindPreference.setWindSharedPreference("m/s", context)
                    selectedUnit = UnitEnum.KELVIN
                },
                label = {
                    Text(stringResource(R.string.kelvin))
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
                },colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    selectedLeadingIconColor = Color.Black,
                    containerColor = Color.Gray,
                    labelColor = Color.White
                )
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
            Image(
                painter = painterResource(R.drawable.wind_icon), contentDescription = null, Modifier
                    .size(width = 50.dp, height = 50.dp)
                    .padding(start = 10.dp, top = 10.dp)
            )
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
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.width(9.dp))
            FilterChip(
                onClick = {},
                enabled = false,
                label = {
                    Text(stringResource(R.string.m_s))
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
                    Text(stringResource(R.string.mph))
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
                },colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    selectedLeadingIconColor = Color.Black,
                    containerColor = Color.Gray,
                    labelColor = Color.White
                )
            )
        }
    }
}

fun restartApp(context: Context) {
    val activity = context as? ComponentActivity
    val intent = activity?.intent
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    activity?.finish()
    activity?.startActivity(intent)
}

fun systemLanguage(context: Context): String {
    //if(LanguagePreference.getLanguagePref(context) == "def")
        return Resources.getSystem().configuration.locales[0].language
    //else return LanguagePreference.getLanguagePref(this) ?: "en"
}