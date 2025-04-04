package com.example.gusty.utilities

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gusty.alarm.AlarmScreen
import com.example.gusty.alarm.AlarmViewModel
import com.example.gusty.favorite.FavoriteScreen
import com.example.gusty.favorite.FavoriteViewModel
import com.example.gusty.home.HomeScreen
import com.example.gusty.home.HomeViewModel


@Composable
fun MyNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    favoriteViewModel: FavoriteViewModel,
    alarmViewModel: AlarmViewModel
) {
    NavHost(navController, startDestination = Routes.HOME.toString()) {
        composable(Routes.HOME.toString()) { HomeScreen(homeViewModel) }
        composable(Routes.FAVORITE.toString()) { FavoriteScreen(favoriteViewModel , homeViewModel)  }
        composable(Routes.ALARM.toString()) { AlarmScreen(alarmViewModel)  }
    }
}