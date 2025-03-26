package com.example.gusty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.gusty.data.remote.GustyRemoteDataSource
import com.example.gusty.data.remote.RetrofitService
import com.example.gusty.data.repo.GustyRepoImpl
import com.example.gusty.home.HomeFactory
import com.example.gusty.home.HomeViewModel
import com.example.gusty.utilities.ButtonNavyItems
import com.example.gusty.utilities.MyNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val factory =
                HomeFactory(GustyRepoImpl.getInstance(GustyRemoteDataSource(RetrofitService.api)))
            val homeViewModel: HomeViewModel = viewModel(factory = factory)

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
                                onClick = { navBarState == index },
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
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    , contentAlignment = Alignment.Center
                ){
                    MyNavGraph(navController , homeViewModel)
                }
            }

        }
    }
}
