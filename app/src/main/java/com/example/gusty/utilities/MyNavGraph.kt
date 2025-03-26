package com.example.gusty.utilities

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gusty.home.HomeScreen
import com.example.gusty.home.HomeViewModel


@Composable
fun MyNavGraph(navController: NavHostController, homeViewModel: HomeViewModel) {
    NavHost(navController, startDestination = "home_screen") {
        composable("home_screen") { HomeScreen(homeViewModel) }
    }
}