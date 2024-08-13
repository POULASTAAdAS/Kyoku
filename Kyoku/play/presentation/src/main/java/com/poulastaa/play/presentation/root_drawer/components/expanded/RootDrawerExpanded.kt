package com.poulastaa.play.presentation.root_drawer.components.expanded

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.domain.TopBarToDrawerEvent
import com.poulastaa.play.presentation.OtherScreens
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistRootScreen
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeCompactScreen
import com.poulastaa.play.presentation.root_drawer.library.LibraryCompactScreen
import com.poulastaa.play.presentation.settings.SettingsRootScreen

@Composable
fun RootDrawerExpanded(
    navController: NavHostController,
    state: RootDrawerUiState,
    onSaveScreenToggle: (SaveScreen) -> Unit,
    onEvent: (RootDrawerUiEvent) -> Unit,
) {
    var toggle by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
        ) {
            ExpandedDrawerContent(
                userName = state.username,
                profilePicUrl = state.profilePicUrl,
                isExpanded = toggle,
                isExpandSmall = false,
                saveScreen = state.saveScreen,
                onExpandToggle = {
                    toggle = !toggle
                },
                navigate = {
                    toggle = false

                    when (it.screen) {
                        ScreenEnum.PROFILE -> navController.navigate(DrawerScreen.Profile.route) {
                            popUpTo(DrawerScreen.Profile.route) { inclusive = true }
                            launchSingleTop = true
                        }

                        ScreenEnum.HISTORY -> navController.navigate(DrawerScreen.History.route) {
                            popUpTo(DrawerScreen.History.route) { inclusive = true }
                            launchSingleTop = true
                        }

                        ScreenEnum.SETTINGS -> navController.navigate(DrawerScreen.Settings.route) {
                            popUpTo(DrawerScreen.Settings.route) { inclusive = true }
                            launchSingleTop = true
                        }

                        else -> onEvent(it)
                    }
                },
                onSaveScreenToggle = onSaveScreenToggle,
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
            }
            else NavHost(
                navController = navController,
                startDestination = state.startDestination
            ) {
                composable(route = DrawerScreen.Home.route) {
                    HomeCompactScreen(
                        profileUrl = state.profilePicUrl,
                        navigate = { screen ->
                            when (screen) {
                                is OtherScreens.AddAsPlaylist -> {
                                    navController.navigate(
                                        route = DrawerScreen.AddToPlaylist.route +
                                                "/${screen.songId}"
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        },
                        onEvent = { event ->
                            when (event) {
                                TopBarToDrawerEvent.PROFILE_CLICK -> Unit

                                TopBarToDrawerEvent.SEARCH_CLICK -> onEvent(
                                    RootDrawerUiEvent.Navigate(
                                        screen = ScreenEnum.HOME_SEARCH
                                    )
                                )
                            }
                        }
                    )
                }

                composable(route = DrawerScreen.Library.route) {
                    LibraryCompactScreen(
                        isExpanded = true,
                        profileUrl = state.profilePicUrl,
                        onProfileClick = {},
                        navigate = {

                        }
                    )
                }

                composable(
                    route = DrawerScreen.AddToPlaylist.route + DrawerScreen.AddToPlaylist.ROUTE_EXT,
                    arguments = listOf(
                        navArgument(DrawerScreen.AddToPlaylist.SONG_ID) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    val id =
                        it.arguments?.getString(DrawerScreen.AddToPlaylist.SONG_ID)?.toLong()
                            ?: -1

                    AddToPlaylistRootScreen(songId = id) {
                        navController.popBackStack()
                    }
                }

                composable(route = DrawerScreen.Profile.route) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Profile",
                            fontSize = MaterialTheme.typography.displayLarge.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                composable(route = DrawerScreen.History.route) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "History",
                            fontSize = MaterialTheme.typography.displayLarge.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                composable(route = DrawerScreen.Settings.route) {
                    SettingsRootScreen(
                        navigate = {
                            when (it) {
                                ScreenEnum.INTRO -> onEvent(
                                    RootDrawerUiEvent.Navigate(
                                        screen = ScreenEnum.INTRO
                                    )
                                )

                                else -> {
                                    navController.popBackStack()
                                    navController.navigate(it.name)
                                }
                            }
                        },
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
