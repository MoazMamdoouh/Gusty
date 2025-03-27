package com.example.gusty.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


const val REQUEST_LOCATION_CODE = 5005
var locationCallback: LocationCallback? = null

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(LocationManager::class.java)

    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true ||
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
}


fun CheckPermission(context: Context): Boolean {

    return ActivityCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}
@SuppressLint("MissingPermission")
fun getUpToDateLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) {
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let { location ->
               // onLocationUpdate(location) // Send updated location
                Log.i("TAG", "map : lat ${location.latitude} , lon ${location.longitude} ")
            }
        }
    }

    val locationRequest = LocationRequest.Builder(5000) // 5 seconds interval
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMinUpdateDistanceMeters(1000F) // 1km movement
        .build()

    fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        locationCallback!!,
        Looper.getMainLooper()
    )
}
