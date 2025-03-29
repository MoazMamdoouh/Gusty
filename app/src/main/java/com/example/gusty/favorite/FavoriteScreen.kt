package com.example.gusty.favorite


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gusty.R
import com.example.gusty.data.local.FavoriteEntity
import com.example.gusty.ui.theme.gray
import com.example.gusty.utilities.LocationPermission
import com.example.gusty.utilities.UiStateResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun FavoriteScreen(favoriteViewModel: FavoriteViewModel) {
    val listOfFavorite = favoriteViewModel.listOfFavorite.collectAsStateWithLifecycle().value
    val loadingState = remember { mutableStateOf(false) }
    val isMapOpen = rememberSaveable { mutableStateOf(false) }

    favoriteViewModel.getListOfFavoriteItems()
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (listOfFavorite) {
                is UiStateResult.Failure -> Log.i(
                    "TAG",
                    "FavoriteScreen: ${listOfFavorite.exception.message} "
                )

                UiStateResult.Loading -> loadingState.value = true
                is UiStateResult.Success -> {
                    loadingState.value = false
                    LazyColumn {
                        itemsIndexed(listOfFavorite.response) { _, favoriteItem ->
                            FavoriteItem(favoriteItem, favoriteViewModel)
                        }
                    }
                }
            }
            if (loadingState.value) CircularProgressIndicator()
        }
        if (isMapOpen.value) {
            GoogleMapView(favoriteViewModel)
        }
    }
}

@Composable
fun FavoriteItem(favoriteEntity: FavoriteEntity, favoriteViewModel: FavoriteViewModel) {
    val openDeleteDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f))
    ) {
        Row {
            Text(
                text = favoriteEntity.countryName,
                Modifier
                    .padding(start = 5.dp)
                    .padding(start = 3.dp, top = 20.dp, bottom = 15.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = ", ${favoriteEntity.cityName}", Modifier
                    .padding(start = 5.dp)
                    .padding(start = 3.dp, top = 20.dp, bottom = 15.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Image(
                painter = painterResource(R.drawable.location),
                contentDescription = "location",
                modifier =
                Modifier
                    .padding(start = 3.dp, top = 15.dp, bottom = 15.dp)
                    .size(width = 20.dp, height = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            val con = LocalContext.current
            Image(imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp, top = 10.dp)
                    .size(24.dp)
                    .clickable {
                        openDeleteDialog.value = true
                        Toast.makeText(con, "Delete", Toast.LENGTH_LONG).show()
                    }
            )
        }
        if (openDeleteDialog.value)
            OpenDeleteFromFavoriteDialog(
                onDismiss = {
                    openDeleteDialog.value = false
                }, favoriteViewModel, favoriteEntity)
    }
}

@Composable
fun GoogleMapView(favoriteViewModel: FavoriteViewModel) {
    val cameraPositionState = rememberCameraPositionState()
    var clickedLocation by remember { mutableStateOf<LatLng?>(null) }
    val openDialogState = remember { mutableStateOf(false) }

    var userLocation = LatLng(
        LocationPermission.locationState.value.latitude,
        LocationPermission.locationState.value.longitude
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
        if (openDialogState.value) OpenAddToFavoriteDialog(onDismiss = {
            openDialogState.value = false
        }, favoriteViewModel, clickedLocation)
    }
}

@Composable
fun OpenAddToFavoriteDialog(
    onDismiss: () -> Unit,
    favoriteViewModel: FavoriteViewModel,
    clickedLocation: LatLng?
) {
    var loadingSate by remember { mutableStateOf(false) }
    var isRequested by remember { mutableStateOf(false) }
    val favoriteCurrentWeatherObject =
        favoriteViewModel.currentWeather.collectAsStateWithLifecycle().value

    LaunchedEffect(favoriteCurrentWeatherObject, isRequested) {
        if (isRequested && clickedLocation != null) {
            when (favoriteCurrentWeatherObject) {
                is UiStateResult.Failure ->
                    Log.i(
                        "TAG", "OpenDialog:${favoriteCurrentWeatherObject.exception.message} "
                    )

                UiStateResult.Loading -> {
                    Log.i("TAG", "OpenDialog:loading ")
                    loadingSate = true
                }

                is UiStateResult.Success -> {
                    Log.i("TAG", "OpenDialog:success before insertion ")
                    favoriteViewModel.insertToFavorite(favoriteCurrentWeatherObject.response)
                    onDismiss.invoke()
                    Log.i("TAG", "insertionDone ")
                }
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                Log.i("TAG", "OpenDialog:button clicked ")
                isRequested = true
                clickedLocation?.let {
                    favoriteViewModel.getCurrentWeatherForFavorite(
                        it.latitude,
                        clickedLocation.longitude
                    )
                }
            }) {
                Text(
                    if (isRequested) "Loading..." else "Add to Favorite",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(text = "Add to favorite")
        },
        text = {
            Text("Do u want to add this Place to Favorite Places" ,
                fontSize = 20.sp,
                color = Color.Black)
        }
    )
}

@Composable
fun OpenDeleteFromFavoriteDialog(
    onDismiss: () -> Unit,
    favoriteViewModel: FavoriteViewModel,
    favoriteEntity: FavoriteEntity
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                favoriteViewModel.deleteLocationFromFavorite(favoriteEntity)
                onDismiss.invoke()
            }) {
                Text("Delete ", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                text = "Delete Location",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Text(
                "Are you sure u want to delete this location ?",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    )
}




