package com.poulastaa.kyoku.navigation.home_navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.HomeScreen
import com.poulastaa.kyoku.presentation.screen.home_root.home.HomeScreenViewModel

@Composable
fun SetupHomeRootNavGraph(
    homeRootUiState: HomeRootUiState,
    navHostController: NavHostController,
    startDestination: String = Screens.Home.route,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
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
                    title = homeRootUiState.homeTopBarTitle,
                    profileUrl = homeRootUiState.profilePicUrl,
                    isCookie = homeRootUiState.isCookie,
                    authHeader = homeRootUiState.headerValue,
                    viewModel = homeScreenViewModel,
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

        composable(Screens.Search.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Search",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            }
        }
    }
}