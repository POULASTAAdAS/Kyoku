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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.domain.TopBarToDrawerEvent
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumOtherScreen
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumRootScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistOtherScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistRootScreen
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistRootScreen
import com.poulastaa.play.presentation.explore_artist.ExploreArtistOtherScreen
import com.poulastaa.play.presentation.explore_artist.ExploreArtistRootScreen
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeCompactScreen
import com.poulastaa.play.presentation.root_drawer.home.HomeOtherScreens
import com.poulastaa.play.presentation.root_drawer.library.LibraryCompactScreen
import com.poulastaa.play.presentation.root_drawer.library.LibraryOtherScreen
import com.poulastaa.play.presentation.settings.SettingsRootScreen
import com.poulastaa.play.presentation.view.ViewCompactScreen
import com.poulastaa.play.presentation.view.ViewOtherScreen
import com.poulastaa.play.presentation.view.components.ViewDataType
import com.poulastaa.play.presentation.view_artist.ViewArtistCompactRootScreen
import com.poulastaa.play.presentation.view_artist.ViewArtistOtherScreen
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
            Box(
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
                                    is HomeOtherScreens.AddAsPlaylist -> onEvent(
                                        RootDrawerUiEvent.AddSongToPlaylist(
                                            screen.songId
                                        )
                                    )

                                    is HomeOtherScreens.View -> onEvent(
                                        RootDrawerUiEvent.View(
                                            screen.id,
                                            screen.type
                                        )
                                    )

                                    is HomeOtherScreens.ViewArtist -> {
                                        Log.d("artistId nav", screen.id.toString())


                                        navController.navigate(
                                            route = DrawerScreen.ViewArtist.route + "/${screen.id}"
                                        )
                                    }
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
                            navigate = { screen ->
                                when (screen) {
                                    is LibraryOtherScreen.View -> onEvent(
                                        RootDrawerUiEvent.View(
                                            screen.id,
                                            screen.type
                                        )
                                    )

                                    is LibraryOtherScreen.ViewArtist -> navController.navigate(
                                        route = DrawerScreen.ViewArtist.route + "/${screen.id}"
                                    )

                                    LibraryOtherScreen.NewAlbum -> onEvent(RootDrawerUiEvent.NewAlbum)

                                    LibraryOtherScreen.NewArtist -> onEvent(RootDrawerUiEvent.NewArtist)
                                }
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

                    composable(
                        route = DrawerScreen.ViewArtist.route + DrawerScreen.ViewArtist.PARAM,
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.LongType
                            }
                        )
                    ) {
                        val id = it.arguments?.getLong("id") ?: -1

                        ViewArtistCompactRootScreen(
                            artistId = id,
                            onArtistDetailScreenOpen = { artistId ->
                                onEvent(RootDrawerUiEvent.OnExploreArtistOpen(artistId))
                            },
                            navigate = { screen ->
                                when (screen) {
                                    is ViewArtistOtherScreen.AddSongToPlaylist -> onEvent(
                                        RootDrawerUiEvent.AddSongToPlaylist(
                                            id = screen.id
                                        )
                                    )

                                    is ViewArtistOtherScreen.ViewArtist -> {

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
                    modifier = Modifier.fillMaxSize(),
                    visible = state.newAlbumUiState.isOpen,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                    exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    AddNewAlbumRootScreen(
                        navigate = {
                            when (it) {
                                is AddNewAlbumOtherScreen.ViewAlbum -> {
                                    onEvent(
                                        RootDrawerUiEvent.View(
                                            id = it.id,
                                            type = ViewDataType.ALBUM
                                        )
                                    )
                                }
                            }
                        },
                        navigateBack = {
                            onEvent(RootDrawerUiEvent.NewAlbumCancel)
                        }
                    )
                }

                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.newArtisUiState.isOpen,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                    exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    AddNewArtistRootScreen(
                        navigate = {
                            when (it) {
                                is AddNewArtistOtherScreen.ViewArtist -> onEvent(
                                    RootDrawerUiEvent.OnExploreArtistOpen(
                                        id = it.id
                                    )
                                )
                            }
                        },
                        navigateBack = {
                            onEvent(RootDrawerUiEvent.NewArtistCancel)
                        }
                    )
                }

                AnimatedVisibility(
                    visible = state.exploreArtistUiState.isOpen,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    ExploreArtistRootScreen(
                        artistId = state.exploreArtistUiState.artistId,
                        navigate = {
                            when (it) {
                                is ExploreArtistOtherScreen.AddSongToPlaylist -> onEvent(
                                    RootDrawerUiEvent.AddSongToPlaylist(
                                        id = it.id
                                    )
                                )

                                is ExploreArtistOtherScreen.ViewAlbum -> {
                                    onEvent(
                                        RootDrawerUiEvent.View(
                                            id = it.id,
                                            type = ViewDataType.ALBUM
                                        )
                                    )
                                }
                            }
                        },
                        navigateBack = {
                            onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
                        }
                    )
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
                        navigate = {
                            when (it) {
                                is ViewOtherScreen.AddSongToPlaylist -> onEvent(
                                    RootDrawerUiEvent.AddSongToPlaylist(
                                        id = it.id
                                    )
                                )

                                is ViewOtherScreen.ViewSongArtists -> {

                                }
                            }
                        },
                        navigateBack = {
                            onEvent(RootDrawerUiEvent.OnViewCancel)
                        }
                    )
                }

                AnimatedVisibility(
                    visible = state.addToPlaylistUiState.isOpen,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    val temp = remember {
                        state.addToPlaylistUiState.isOpen
                    }

                    if (temp) AddToPlaylistRootScreen(songId = state.addToPlaylistUiState.songId) {
                        onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
                    }
                }
            }

            if (state.addToPlaylistUiState.isOpen ||
                state.viewUiState.isOpen ||
                state.exploreArtistUiState.isOpen ||
                state.newArtisUiState.isOpen ||
                state.newAlbumUiState.isOpen
            ) BackHandler {
                if (state.addToPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
                else if (state.viewUiState.isOpen) onEvent(RootDrawerUiEvent.OnViewCancel)
                else if (state.exploreArtistUiState.isOpen) onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
                else if (state.newArtisUiState.isOpen) onEvent(RootDrawerUiEvent.NewArtistCancel)
                else if (state.newAlbumUiState.isOpen) onEvent(RootDrawerUiEvent.NewAlbumCancel)
            }
        }
    )
}