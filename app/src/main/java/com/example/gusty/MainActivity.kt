package com.example.gusty

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.gusty.alarm.AlarmFactory
import com.example.gusty.alarm.AlarmViewModel
import com.example.gusty.data.local.FavoriteDataBase
import com.example.gusty.data.local.GustyLocalDataSourceImpl
import com.example.gusty.data.remote.GustyRemoteDataSourceImpl
import com.example.gusty.data.remote.RetrofitService
import com.example.gusty.data.repo.GustyRepoImpl
import com.example.gusty.favorite.FavoriteFactory
import com.example.gusty.favorite.FavoriteViewModel
import com.example.gusty.home.HomeFactory
import com.example.gusty.home.HomeViewModel
import com.example.gusty.utilities.ButtonNavyItems
import com.example.gusty.utilities.LocationPermission
import com.example.gusty.utilities.MyNavGraph
import com.example.gusty.utilities.Routes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val factory =
                HomeFactory(
                    GustyRepoImpl.getInstance(
                        GustyRemoteDataSourceImpl.getInstance(RetrofitService.api),
                        GustyLocalDataSourceImpl.getInstance(
                            FavoriteDataBase.getInstance(this).getProductsDao(),
                            FavoriteDataBase.getInstance(this).getAlarmDao()
                        )
                    )
                )
            val homeViewModel: HomeViewModel = viewModel(factory = factory)

            val favoriteFactory =
                FavoriteFactory(
                    GustyRepoImpl.getInstance(
                        GustyRemoteDataSourceImpl.getInstance(RetrofitService.api),
                        GustyLocalDataSourceImpl.getInstance(
                            FavoriteDataBase.getInstance(this).getProductsDao(),
                            FavoriteDataBase.getInstance(this).getAlarmDao()
                        )
                    )
                )
            val favoriteViewModel: FavoriteViewModel = viewModel(factory = favoriteFactory)

            val alarmFactory =
                AlarmFactory(
                    GustyRepoImpl.getInstance(
                        GustyRemoteDataSourceImpl.getInstance(RetrofitService.api),
                        GustyLocalDataSourceImpl.getInstance(
                            FavoriteDataBase.getInstance(this).getProductsDao(),
                            FavoriteDataBase.getInstance(this).getAlarmDao()
                        )
                    )
                )
            val alarmViewModel : AlarmViewModel = viewModel(factory = alarmFactory)

            val buttonNavItems = listOf(
                ButtonNavyItems(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unSelectedIcon = Icons.Outlined.Home
                ),
                ButtonNavyItems(
                    title = "Favorite",
                    selectedIcon = Icons.Filled.Favorite,
                    unSelectedIcon = Icons.Outlined.FavoriteBorder
                ),
                ButtonNavyItems(
                    title = "Notification",
                    selectedIcon = Icons.Filled.Notifications,
                    unSelectedIcon = Icons.Outlined.Notifications
                ),
                ButtonNavyItems(
                    title = "Sittings",
                    selectedIcon = Icons.Filled.Settings,
                    unSelectedIcon = Icons.Outlined.Settings
                )
            )
            var navBarState by rememberSaveable { mutableIntStateOf(0) }
            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar {
                        buttonNavItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = navBarState == index,
                                onClick = {
                                    navBarState = index
                                    when (index) {
                                        0 -> navController.navigate(Routes.HOME.toString())
                                        1 -> navController.navigate(Routes.FAVORITE.toString())
                                        2 -> navController.navigate(Routes.ALARM.toString())
                                        3 -> navController.navigate(Routes.SETTINGS.toString())
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (navBarState == index) item.selectedIcon
                                        else item.unSelectedIcon,
                                        contentDescription = null
                                    )
                                }, label = {
                                    Text(text = item.title)
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding), contentAlignment = Alignment.Center
                ) {
                    MyNavGraph(navController, homeViewModel, favoriteViewModel , alarmViewModel ,)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (LocationPermission.CheckPermission(this)) {
            if (LocationPermission.isLocationEnabled(this)) {
                LocationPermission.getUpToDateLocation(fusedLocationProvider, context = this)
                Log.i("TAG", "onStart: location gotted")
            } else {
                // enableLocationService()
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), LocationPermission.REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        when (requestCode) {
            LocationPermission.REQUEST_LOCATION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (LocationPermission.isLocationEnabled(this)) {
                        LocationPermission.getUpToDateLocation(fusedLocationProvider, this)
                    } else {
                        //  enableLocationService()
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ), LocationPermission.REQUEST_LOCATION_CODE
                    )
                }
            }
        }
    }
}
