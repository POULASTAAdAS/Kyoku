package com.poulastaa.play.presentation.root_drawer.components.compact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.HomeToDrawerEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeCompactScreen
import kotlinx.coroutines.launch

@Composable
fun RootDrawerCompact(
    drawerState: DrawerState,
    navController: NavHostController,
    state: RootDrawerUiState,
    onEvent: (RootDrawerUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            CompactDrawerContent(
                userName = state.username,
                profilePicUrl = state.profilePicUrl,
                navigate = {
                    scope.launch {
                        drawerState.close()
                    }

                    when (it.screen) {
                        ScreenEnum.PROFILE -> navController.navigate(DrawerScreen.Profile.route)

                        ScreenEnum.HISTORY -> navController.navigate(DrawerScreen.History.route)

                        ScreenEnum.SETTINGS -> navController.navigate(DrawerScreen.Settings.route)

                        else -> onEvent(it)
                    }
                },
                onLogoutClick = {
                    onEvent(RootDrawerUiEvent.LogOut)
                }
            )
        },
        content = {
            if (!state.isScreenLoaded) Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = MaterialTheme.colorScheme.primary.copy(.6f),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 5.dp
                )
            } else NavHost(
                navController = navController,
                startDestination = state.startDestination
            ) {
                composable(route = DrawerScreen.Home.route) {
                    HomeCompactScreen(
                        profileUrl = state.profilePicUrl,
                        onEvent = {
                            when (it) {
                                HomeToDrawerEvent.PROFILE_CLICK -> onEvent(RootDrawerUiEvent.OnDrawerToggle)

                                HomeToDrawerEvent.SEARCH_CLICK -> onEvent(
                                    RootDrawerUiEvent.Navigate(
                                        screen = ScreenEnum.HOME_SEARCH
                                    )
                                )
                            }
                        }
                    )
                }

                composable(route = DrawerScreen.Library.route) {

                }

                composable(route = DrawerScreen.Profile.route) {

                }

                composable(route = DrawerScreen.History.route) {

                }

                composable(route = DrawerScreen.Settings.route) {

                }
            }
        }
    )
}