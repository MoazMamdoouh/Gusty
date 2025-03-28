package com.example.gusty.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gusty.ui.theme.gray
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun FavoriteScreen() {
    Column {
        FloatingButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun FloatingButton() {
    var isMapOpen = rememberSaveable { mutableStateOf(false) }


    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isMapOpen.value = true
                }, containerColor = gray
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        }
    ){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (isMapOpen.value) {
                GoogleMapView()
            }
        }
    }

}

@Composable
fun GoogleMapView() {
    val cameraPositionState = rememberCameraPositionState()
    var clickedLocation by remember { mutableStateOf<LatLng?>(null) }
    Column (
        modifier = Modifier.fillMaxSize()
    )   {
        GoogleMap(
            modifier = Modifier.fillMaxSize() ,
            cameraPositionState = cameraPositionState ,
            onMapClick = {
//                Log.i("TAG", "map : lat ${it.latitude} , lon ${it.longitude} ")
                clickedLocation = it
            }
        ) {
            clickedLocation?.let {
                Marker(
                    state = MarkerState(position = it) ,
                    title = "Clicked place " ,

                    )
            }
        }
    }
}


