package com.poulastaa.kyoku.navigation.home_navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.HomeScreen

@Composable
fun SetupHomeRootNavGraph(
    navHostController: NavHostController,
    startDestination: String = Screens.Home.route,
    opnDrawer: () -> Unit,
    handleUiEvent: (HomeRootUiEvent) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(Screens.Home.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HomeScreen(
                    opnDrawer = opnDrawer,
                    navigate = handleUiEvent
                )
            }
        }

        composable(Screens.Settings.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Settings",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            }
        }

        composable(Screens.Profile.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Profile",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            }
        }

        composable(Screens.History.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "History",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            }
        }
    }
}