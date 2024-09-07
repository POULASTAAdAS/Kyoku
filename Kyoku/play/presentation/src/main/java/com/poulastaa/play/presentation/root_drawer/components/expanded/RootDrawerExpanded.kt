package com.poulastaa.play.presentation.root_drawer.components.expanded

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.platform.LocalConfiguration
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
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumOtherScreen
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumRootScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistOtherScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistRootScreen
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistRootScreen
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistRootScreen
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
import com.poulastaa.play.presentation.view_artist.ViewArtistExpandedRootScreen
import com.poulastaa.play.presentation.view_artist.ViewArtistOtherScreen

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

    val config = LocalConfiguration.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
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

        Surface {
            Box {
                Row {
                    NavHost(
                        navController = navController,
                        startDestination = state.startDestination,
                        modifier = Modifier
                            .then(
                                if (config.screenWidthDp > 980 &&
                                    (state.addToPlaylistUiState.isOpen ||
                                            state.viewUiState.isOpen ||
                                            state.newAlbumUiState.isOpen ||
                                            state.newArtisUiState.isOpen ||
                                            state.createPlaylistUiState.isOpen ||
                                            state.exploreArtistUiState.isOpen)
                                ) Modifier
                                    .fillMaxWidth(.6f)
                                    .fillMaxHeight()
                                else Modifier.fillMaxSize()
                            )
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
                                            navController.navigate(
                                                route = DrawerScreen.ViewArtist.route + "/${screen.id}"
                                            )
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

                            if (state.addToPlaylistUiState.isOpen ||
                                state.viewUiState.isOpen ||
                                state.newAlbumUiState.isOpen ||
                                state.newArtisUiState.isOpen ||
                                state.createPlaylistUiState.isOpen ||
                                (state.exploreArtistUiState.isOpen && config.screenWidthDp > 980)  // stopping compact view for compact screen
                            ) ViewArtistCompactRootScreen(
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
                            else ViewArtistExpandedRootScreen(
                                artistId = id,
                                navigateToArtistDetail = { artistId ->
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

                    // expand screen
                    AnimatedVisibility(
                        visible = state.addToPlaylistUiState.isOpen && config.screenWidthDp > 980,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        AddToPlaylistRootScreen(
                            songId = state.addToPlaylistUiState.songId,
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small2)
                        ) {
                            onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
                        }
                    }

                    AnimatedVisibility(
                        visible = state.viewUiState.isOpen && config.screenWidthDp > 960,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ViewCompactScreen(
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                            id = state.viewUiState.songId,
                            type = state.viewUiState.type,
                            navigate = {
                                when (it) {
                                    is ViewOtherScreen.AddSongToPlaylist -> onEvent(
                                        RootDrawerUiEvent.AddSongToPlaylist(
                                            id = it.id
                                        )
                                    )

                                    is ViewOtherScreen.CreatePlaylistScreen -> onEvent(
                                        RootDrawerUiEvent.CreatePlaylist(
                                            playlistId = it.playlistId
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
                        modifier = Modifier.fillMaxSize(),
                        visible = state.newAlbumUiState.isOpen && config.screenWidthDp > 980,
                        enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                    ) {
                        AddNewAlbumRootScreen(
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
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
                        visible = state.exploreArtistUiState.isOpen && config.screenWidthDp > 960,
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        ExploreArtistRootScreen(
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                            artistId = state.exploreArtistUiState.artistId,
                            navigate = {
                                when (it) {
                                    is ExploreArtistOtherScreen.AddSongToPlaylist -> onEvent(
                                        RootDrawerUiEvent.AddSongToPlaylist(
                                            id = it.id
                                        )
                                    )

                                    is ExploreArtistOtherScreen.ViewAlbum -> onEvent(
                                        RootDrawerUiEvent.View(
                                            id = it.id,
                                            type = ViewDataType.ALBUM
                                        )
                                    )
                                }
                            },
                            navigateBack = {
                                onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
                            }
                        )
                    }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = state.newArtisUiState.isOpen && config.screenWidthDp > 980,
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
                        modifier = Modifier.fillMaxSize(),
                        visible = state.createPlaylistUiState.isOpen && config.screenWidthDp > 980,
                        enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                    ) {
                        CreatePlaylistRootScreen(
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                            playlistId = state.createPlaylistUiState.playlistId,
                            navigateBack = {
                                onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
                            }
                        )
                    }
                }

                // medium screen
                this@Row.AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.newAlbumUiState.isOpen && config.screenWidthDp < 980,
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

                this@Row.AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.newArtisUiState.isOpen && config.screenWidthDp < 980,
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

                this@Row.AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.exploreArtistUiState.isOpen && config.screenWidthDp < 980,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    ExploreArtistRootScreen(
                        modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                        artistId = state.exploreArtistUiState.artistId,
                        navigate = {
                            when (it) {
                                is ExploreArtistOtherScreen.AddSongToPlaylist -> {

                                }

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

                this@Row.AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.viewUiState.isOpen && config.screenWidthDp < 980,
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

                                is ViewOtherScreen.CreatePlaylistScreen -> onEvent(
                                    RootDrawerUiEvent.CreatePlaylist(
                                        playlistId = it.playlistId
                                    )
                                )


                                is ViewOtherScreen.ViewSongArtists -> {
                                    // todo add screen
                                }
                            }
                        },
                        navigateBack = {
                            onEvent(RootDrawerUiEvent.OnViewCancel)
                        }
                    )
                }

                this@Row.AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = state.addToPlaylistUiState.isOpen && config.screenWidthDp < 980,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    AddToPlaylistRootScreen(songId = state.addToPlaylistUiState.songId) {
                        onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
                    }
                }
            }
        }
    }

    if (state.addToPlaylistUiState.isOpen ||
        state.viewUiState.isOpen ||
        state.exploreArtistUiState.isOpen ||
        state.newArtisUiState.isOpen ||
        state.newAlbumUiState.isOpen ||
        state.createPlaylistUiState.isOpen
    ) BackHandler {
        if (state.addToPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
        else if (state.viewUiState.isOpen) onEvent(RootDrawerUiEvent.OnViewCancel)
        else if (state.exploreArtistUiState.isOpen) onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
        else if (state.newArtisUiState.isOpen) onEvent(RootDrawerUiEvent.NewArtistCancel)
        else if (state.newAlbumUiState.isOpen) onEvent(RootDrawerUiEvent.NewAlbumCancel)
        else if (state.createPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
    }
}
