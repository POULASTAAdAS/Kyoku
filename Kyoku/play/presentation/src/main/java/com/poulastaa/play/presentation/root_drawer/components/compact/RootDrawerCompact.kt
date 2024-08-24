package com.poulastaa.play.presentation.root_drawer.components.compact

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.domain.TopBarToDrawerEvent
import com.poulastaa.play.presentation.OtherScreens
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistRootScreen
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeCompactScreen
import com.poulastaa.play.presentation.root_drawer.library.LibraryCompactScreen
import com.poulastaa.play.presentation.settings.SettingsRootScreen
import com.poulastaa.play.presentation.view.ViewCompactScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun RootDrawerCompact(
    drawerState: DrawerState,
    navController: NavHostController,
    state: RootDrawerUiState,
    onSaveScreenToggle: (SaveScreen) -> Unit,
    onEvent: (RootDrawerUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var currentDestination by remember {
        mutableStateOf(state.startDestination)
    }

    LaunchedEffect(key1 = navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collectLatest {
            it.destination.route?.let { route ->
                currentDestination = route
            }
        }
    }

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
            } else Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = state.startDestination
                ) {
                    composable(route = DrawerScreen.Home.route) {
                        HomeCompactScreen(
                            profileUrl = state.profilePicUrl,
                            navigate = { screen ->
                                when (screen) {
                                    is OtherScreens.AddAsPlaylist -> onEvent(
                                        RootDrawerUiEvent.AddSongToPlaylist(
                                            screen.songId
                                        )
                                    )

                                    is OtherScreens.View -> onEvent(
                                        RootDrawerUiEvent.View(
                                            screen.id,
                                            screen.type
                                        )
                                    )
                                }
                            },
                            onEvent = { event ->
                                when (event) {
                                    TopBarToDrawerEvent.PROFILE_CLICK -> onEvent(RootDrawerUiEvent.OnDrawerToggle)

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
                            profileUrl = state.profilePicUrl,
                            onProfileClick = {
                                onEvent(RootDrawerUiEvent.OnDrawerToggle)
                            },
                            navigate = {

                            }
                        )
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

                CompactBottomNavigation(
                    currentDestination = currentDestination,
                    saveScreen = state.saveScreen,
                    onSaveScreenToggle = onSaveScreenToggle
                )

                AnimatedVisibility(
                    visible = state.addToPlaylistUiState.isOpen,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    val temp = remember {
                        state.addToPlaylistUiState.isOpen
                    }

                    if (temp) AddToPlaylistRootScreen(songId = state.addToPlaylistUiState.songId) {
                        onEvent(RootDrawerUiEvent.AddSongToPlaylistCancel)
                    }
                }

                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.viewUiState.isOpen,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                    exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    ViewCompactScreen(
                        id = state.viewUiState.songId,
                        type = state.viewUiState.type,
                        navigateBack = {
                            onEvent(RootDrawerUiEvent.ViewCancel)
                        }
                    )
                }
            }

            if (state.addToPlaylistUiState.isOpen || state.viewUiState.isOpen) BackHandler {
                if (state.addToPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.AddSongToPlaylistCancel)
                else if (state.viewUiState.isOpen) onEvent(RootDrawerUiEvent.ViewCancel)
            }
        }
    )
}