package com.example.gusty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gusty.data.remote.GustyRemoteDataSource
import com.example.gusty.data.remote.RetrofitService
import com.example.gusty.data.repo.GustyRepoImpl
import com.example.gusty.home.HomeFactory
import com.example.gusty.home.HomeScreen
import com.example.gusty.home.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val factory =
                HomeFactory(GustyRepoImpl.getInstance(GustyRemoteDataSource(RetrofitService.api)))
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            homeViewModel.getCurrentWeather()
            val navController = rememberNavController()
            NavHost(navController, startDestination = "home_screen") {
                composable("home_screen") { HomeScreen(homeViewModel) }
            }
        }
    }
}
