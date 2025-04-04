package com.example.gusty.setting

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.dark_blue
import com.example.gusty.utilities.LocationPermission
import com.example.gusty.utilities.Routes


@Composable
fun SettingScreen(navController: NavHostController) {

    Column {
        LanguageCard()
        LocationCard(navController)

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
                modifier = Modifier.padding( top = 13.dp , start = 10.dp)
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
                fontSize = 18.sp ,
                modifier = Modifier.padding(top = 13.dp )
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

    if(Preference.getLocationStateSharedPreference(context).equals("gps")){
        gpsChecked = true
    }else {
        gpsChecked =  false
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
                modifier = Modifier.padding( top = 13.dp , start = 10.dp)
            )
            Switch(
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                checked = gpsChecked,
                onCheckedChange = {
                    Preference.setLocationStateSharedPreference("gps" , context)
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
                fontSize = 18.sp ,
                modifier = Modifier.padding(top = 13.dp)
            )
            Switch(
                modifier = Modifier.padding(start = 5.dp),
                checked = locationChecked,
                onCheckedChange = {
                    if(it){
                        Preference.setLocationStateSharedPreference("location" , context)
                    }
                    locationChecked = it
                }, thumbContent = if (Preference.getLocationStateSharedPreference(context).equals("Location")) {
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
            if(Preference.getLocationStateSharedPreference(context).equals("gps")){
                Preference.setLatitudeSharedPreference(LocationPermission.locationState.value.latitude , context)
                Preference.setLongitudeSharedPreference(LocationPermission.locationState.value.longitude , context)
            }
            if(locationChecked){
                navController.navigate(Routes.SETTINGS_MAP.toString())
                locationChecked = false
            }

        }
    }
}
