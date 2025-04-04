package com.example.gusty.setting

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GoogleMapViewOnSettings() {
    val cameraPositionState = rememberCameraPositionState()
    var clickedLocation by remember { mutableStateOf<LatLng?>(null) }
    val openDialogState = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var userLocation = LatLng(
        Preference.getLatitudeSharedPreference(context),
        Preference.getLongitudeSharedPreference(context)
    )
    LaunchedEffect(Unit) {
        if (clickedLocation == null) {
            clickedLocation = userLocation
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                clickedLocation = it
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(it)
                )
                openDialogState.value = true
                Log.i("TAG", "On map clicked ")
                clickedLocation?.let { newClick ->
                    clickedLocation = LatLng(newClick.latitude, newClick.longitude)
                }
            }
        ) {
            clickedLocation?.let { newClick ->
                Marker(
                    state = MarkerState(position = newClick),
                    title = "Clicked place ",
                )
            }
        }
        if (openDialogState.value) OpenSetAsDefaultDialog(onDismiss = {
            openDialogState.value = false
        }, clickedLocation , context)
    }
}


@Composable
fun OpenSetAsDefaultDialog(
    onDismiss: () -> Unit,
    clickedLocation: LatLng?,
    context: Context
) {
    var isRequested by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                Log.i("TAG", "OpenDialog:button clicked ")
                isRequested = true
                clickedLocation?.let {
                    Preference.setLatitudeSharedPreference( it.latitude , context)
                    Preference.setLongitudeSharedPreference(it.longitude , context)
                }
                onDismiss.invoke()
            }) {
                Text(
                    if (isRequested) "Loading..." else "set as Default",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            }
        }
        ,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel" , color = Color.Black)
            }
        },
        title = {
            Text(text = "set as Default !!" , color = Color.Black , fontSize = 32.sp )
        }
        ,
        text = {
            Text("Are u sure u want to set this location as default" ,
                fontSize = 20.sp,
                color = Color.Black)
        }
    )
}