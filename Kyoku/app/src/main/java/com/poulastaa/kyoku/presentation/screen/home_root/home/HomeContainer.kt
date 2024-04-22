package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar
import com.poulastaa.kyoku.presentation.screen.home_root.library.LibraryScreen
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContainer(
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        ),
    isSmallPhone: Boolean = LocalConfiguration.current.screenWidthDp <= 411,
    context: Context = LocalContext.current,
    state: HomeRootUiState,
    opnDrawer: () -> Unit,
    nav: (HomeRootUiEvent) -> Unit,
    navigateWithUiEvent: (HomeRootUiEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            when (state.nav) {
                Nav.HOME -> HomeTopAppBar(
                    title = state.homeTopBarTitle,
                    profileUrl = profileUrl,
                    isCookie = isCookie,
                    authHeader = authHeader,
                    scrollBehavior = topAppBarScrollBehavior,
                    isSmallPhone = isSmallPhone,
                    onProfileClick = opnDrawer,
                    onSearchClick = navigateWithUiEvent
                )

                Nav.LIB -> LibraryTopAppBar(
                    title = state.libraryTopBarTitle,
                    profileUrl = profileUrl,
                    isCookie = isCookie,
                    authHeader = authHeader,
                    isSmallPhone = isSmallPhone,
                    scrollBehavior = topAppBarScrollBehavior,
                    onProfileClick = opnDrawer,
                    onSearchClick = navigateWithUiEvent
                )

                else -> Unit
            }
        }
    ) { paddingValue ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route
        ) {
            composable(
                route = Screens.Home.route
            ) {
                HomeScreen(
                    isCookie = isCookie,
                    authHeader = authHeader,
                    isSmallPhone = isSmallPhone,
                    context = context,
                    paddingValues = paddingValue,
                    navigate = {

                    },
                    update = {
                        nav.invoke(HomeRootUiEvent.Update(Screens.Home))
                    }
                )
            }

            composable(Screens.Library.route) {
                LibraryScreen(
                    isSmallPhone = isSmallPhone,
                    context = context,
                    paddingValues = paddingValue,
                    isCookie = isCookie,
                    headerValue = authHeader,
                    navigate = {

                    },
                    update = {
                        nav.invoke(HomeRootUiEvent.Update(Screens.Library))
                    }
                )
            }

            composable(Screens.Search.route) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Search")
                }
            }

            composable(Screens.Profile.route) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Profile")
                }
            }


            composable(Screens.Settings.route) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Settings")
                }
            }

            composable(Screens.History.route) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "History")
                }
            }
        }
    }
}