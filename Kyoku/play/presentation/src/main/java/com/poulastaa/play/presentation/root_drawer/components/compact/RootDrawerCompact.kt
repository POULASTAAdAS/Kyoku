package com.poulastaa.play.presentation.root_drawer.components.compact

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
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
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumOtherScreen
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumRootScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistOtherScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistRootScreen
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistRootScreen
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistRootScreen
import com.poulastaa.play.presentation.explore_artist.ExploreArtistOtherScreen
import com.poulastaa.play.presentation.explore_artist.ExploreArtistRootScreen
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiState
import com.poulastaa.play.presentation.player.full_player.VerticalPlayerScreen
import com.poulastaa.play.presentation.player.small_player.SmallCompactPlayer
import com.poulastaa.play.presentation.profile.ProfilePortraitRootScreen
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeCompactScreen
import com.poulastaa.play.presentation.root_drawer.home.HomeOtherScreens
import com.poulastaa.play.presentation.root_drawer.library.LibraryCompactScreen
import com.poulastaa.play.presentation.root_drawer.library.LibraryOtherScreen
import com.poulastaa.play.presentation.root_drawer.toPlayType
import com.poulastaa.play.presentation.settings.SettingsRootScreen
import com.poulastaa.play.presentation.song_artist.SongArtistsBottomSheet
import com.poulastaa.play.presentation.view.ViewCompactScreen
import com.poulastaa.play.presentation.view.ViewOtherScreen
import com.poulastaa.play.presentation.view.components.ViewDataType
import com.poulastaa.play.presentation.view_artist.ViewArtistCompactRootScreen
import com.poulastaa.play.presentation.view_artist.ViewArtistOtherScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootDrawerCompact(
    viewSongArtistSheetState: SheetState,
    isSmall: Boolean,
    drawerState: DrawerState,
    navController: NavHostController,
    drawerUiState: RootDrawerUiState,
    playerUiState: PlayerUiState,
    onSaveScreenToggle: (SaveScreen) -> Unit,
    onEvent: (RootDrawerUiEvent) -> Unit,
    onPlayerEvent: (PlayerUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var currentDestination by remember {
        mutableStateOf(drawerUiState.startDestination)
    }
    var dragScope by remember { mutableFloatStateOf(0f) }
    val smallPlayerPadding = animateDpAsState(
        if (
            (drawerUiState.viewUiState.isOpen ||
                    drawerUiState.addToPlaylistUiState.isOpen ||
                    drawerUiState.newArtisUiState.isOpen ||
                    drawerUiState.newAlbumUiState.isOpen ||
                    drawerUiState.exploreArtistUiState.isOpen)
            && playerUiState.isData
        ) 100.dp else 0.dp,
        label = ""
    )

    LaunchedEffect(key1 = navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collectLatest {
            it.destination.route?.let { route ->
                currentDestination = route
            }
        }
    }

    ModalNavigationDrawer(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding(),
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            CompactDrawerContent(
                userName = drawerUiState.username,
                profilePicUrl = drawerUiState.profilePicUrl,
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
            Box {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                        .fillMaxSize()
                        .padding(bottom = smallPlayerPadding.value)
                ) {
                    NavHost(
                        modifier = Modifier
                            .padding(bottom = if (playerUiState.isData) 100.dp else 0.dp),
                        navController = navController,
                        startDestination = drawerUiState.startDestination
                    ) {
                        composable(route = DrawerScreen.Home.route) {
                            HomeCompactScreen(
                                profileUrl = drawerUiState.profilePicUrl,
                                navigate = { event ->
                                    when (event) {
                                        is HomeOtherScreens.AddAsPlaylist -> onEvent(
                                            RootDrawerUiEvent.AddSongToPlaylist(
                                                event.songId
                                            )
                                        )

                                        is HomeOtherScreens.View -> onEvent(
                                            RootDrawerUiEvent.View(
                                                event.id,
                                                event.type
                                            )
                                        )

                                        is HomeOtherScreens.ViewArtist -> {
                                            navController.navigate(
                                                route = DrawerScreen.ViewArtist.route + "/${event.artistId}"
                                            )
                                        }

                                        is HomeOtherScreens.ViewSongArtist -> {
                                            onEvent(RootDrawerUiEvent.OnViewSongArtists(event.songId))

                                            scope.launch {
                                                viewSongArtistSheetState.show()
                                            }
                                        }
                                    }
                                },
                                onEvent = { event ->
                                    when (event) {
                                        TopBarToDrawerEvent.PROFILE_CLICK -> onEvent(
                                            RootDrawerUiEvent.OnDrawerToggle
                                        )

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
                                profileUrl = drawerUiState.profilePicUrl,
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
                            ProfilePortraitRootScreen(
                                navigateToLibrary = {
                                    navController.navigate(DrawerScreen.Library.route) {
                                        popUpTo(DrawerScreen.Library.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                navigateBack = {
                                    navController.popBackStack()
                                }
                            )
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
                        saveScreen = drawerUiState.saveScreen,
                        onSaveScreenToggle = onSaveScreenToggle
                    )

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = drawerUiState.newAlbumUiState.isOpen,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
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
                        visible = drawerUiState.newArtisUiState.isOpen,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
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
                        visible = drawerUiState.exploreArtistUiState.isOpen,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                    ) {
                        ExploreArtistRootScreen(
                            artistId = drawerUiState.exploreArtistUiState.artistId,
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
                        visible = drawerUiState.viewUiState.isOpen,
                        enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                    ) {
                        ViewCompactScreen(
                            id = drawerUiState.viewUiState.songId,
                            type = drawerUiState.viewUiState.type,
                            navigate = { event ->
                                when (event) {
                                    is ViewOtherScreen.AddSongToPlaylist -> onEvent(
                                        RootDrawerUiEvent.AddSongToPlaylist(
                                            id = event.id
                                        )
                                    )

                                    is ViewOtherScreen.CreatePlaylistScreen -> onEvent(
                                        RootDrawerUiEvent.CreatePlaylist(
                                            playlistId = event.playlistId
                                        )
                                    )

                                    is ViewOtherScreen.ViewSongArtists -> {
                                        onEvent(RootDrawerUiEvent.OnViewSongArtists(event.id))

                                        scope.launch {
                                            viewSongArtistSheetState.show()
                                        }
                                    }

                                    is ViewOtherScreen.PlayOperation.PlayAll -> onPlayerEvent(
                                        PlayerUiEvent.PlayOperation.PlayAll(
                                            id = event.id,
                                            type = event.type.toPlayType()
                                        )
                                    )

                                    is ViewOtherScreen.PlayOperation.Shuffle -> onPlayerEvent(
                                        PlayerUiEvent.PlayOperation.ShuffleAll(
                                            id = event.id,
                                            type = event.type.toPlayType()
                                        )
                                    )

                                    is ViewOtherScreen.PlayOperation.PlayOne -> onPlayerEvent(
                                        PlayerUiEvent.PlayOperation.PlayOne(
                                            songId = event.songId,
                                            otherId = event.otherId,
                                            type = event.type.toPlayType()
                                        )
                                    )
                                }
                            },
                            navigateBack = {
                                onEvent(RootDrawerUiEvent.OnViewCancel)
                            }
                        )
                    }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = drawerUiState.addToPlaylistUiState.isOpen,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                    ) {
                        AddToPlaylistRootScreen(songId = drawerUiState.addToPlaylistUiState.songId) {
                            onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
                        }
                    }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = drawerUiState.createPlaylistUiState.isOpen,
                        enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
                    ) {
                        CreatePlaylistRootScreen(
                            playlistId = drawerUiState.createPlaylistUiState.playlistId,
                            navigateBack = {
                                onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
                            }
                        )
                    }
                }

                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = MaterialTheme.dimens.medium1)
                        .padding(
                            bottom = if (
                                drawerUiState.viewUiState.isOpen ||
                                drawerUiState.addToPlaylistUiState.isOpen ||
                                drawerUiState.newArtisUiState.isOpen ||
                                drawerUiState.newAlbumUiState.isOpen ||
                                drawerUiState.exploreArtistUiState.isOpen ||
                                navController.currentDestination?.route?.contains(DrawerScreen.ViewArtist.route) == true ||
                                navController.currentDestination?.route?.contains(DrawerScreen.Profile.route) == true ||
                                navController.currentDestination?.route?.contains(DrawerScreen.History.route) == true ||
                                navController.currentDestination?.route?.contains(DrawerScreen.Settings.route) == true
                            ) 0.dp else 45.dp
                        )
                        .offset {
                            IntOffset(
                                x = 0,
                                dragScope
                                    .coerceAtLeast(0f)
                                    .toInt()
                            )
                        }
                        .draggable(
                            state = rememberDraggableState {
                                dragScope += it
                            },
                            orientation = Orientation.Vertical,
                            onDragStopped = {
                                if (dragScope > 130) onPlayerEvent(PlayerUiEvent.ClosePlayer)
                                dragScope = 0f
                            },
                        )
                        .navigationBarsPadding()
                        .imePadding()
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onPlayerEvent(PlayerUiEvent.OnPlayerExtendClick)
                        },
                    visible = playerUiState.isData &&
                            playerUiState.queue.isNotEmpty() &&
                            !playerUiState.isPlayerExtended,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                            slideInVertically(tween(400)) { it },
                    exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center) +
                            slideOutVertically(tween(400)) { it }
                ) {
                    if (playerUiState.queue.isNotEmpty()) SmallCompactPlayer(
                        modifier = Modifier.padding(
                            if (isSmall) MaterialTheme.dimens.small3 else MaterialTheme.dimens.small1
                        ),
                        header = drawerUiState.header,
                        height = if (isSmall) 95.dp else 120.dp,
                        song = playerUiState.queue[playerUiState.info.currentPlayingIndex].copy(
                            colors = playerUiState.queue[playerUiState.info.currentPlayingIndex].colors.ifEmpty {
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.surfaceContainer
                                )
                            }
                        ),
                        info = playerUiState.info,
                        onEvent = onPlayerEvent
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(x = 0, dragScope.toInt())
                    },
                visible = playerUiState.isPlayerExtended,
                enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                        slideInVertically(tween(400)) { it },
                exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center) +
                        slideOutVertically(tween(400)) { it }
            ) {
                VerticalPlayerScreen(
                    header = drawerUiState.header,
                    song = playerUiState.queue[playerUiState.info.currentPlayingIndex].copy(
                        colors = playerUiState.queue[playerUiState.info.currentPlayingIndex].colors.ifEmpty {
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.surfaceContainer
                            )
                        }
                    ),
                    info = playerUiState.info,
                    queue = playerUiState.queue,
                    onEvent = {
                        if (it is PlayerUiEvent.OnArtistClick) {
                            navController.navigate(
                                route = DrawerScreen.ViewArtist.route + "/${it.artistId}"
                            )

                            onPlayerEvent(PlayerUiEvent.OnPlayerShrinkClick)
                        } else onPlayerEvent(it)
                    }
                )
            }
        }
    )

    if (viewSongArtistSheetState.isVisible) SongArtistsBottomSheet(
        songId = drawerUiState.viewSongArtistSongId,
        sheetState = viewSongArtistSheetState,
        navigateToArtistScreen = {
            scope.launch {
                viewSongArtistSheetState.hide()
            }.invokeOnCompletion {
                onEvent(RootDrawerUiEvent.OnViewSongArtistsCancel)
                onEvent(RootDrawerUiEvent.OnViewCancel)
                onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
            }

            navController.navigate(
                route = DrawerScreen.ViewArtist.route + "/${it}"
            )
        },
        navigateBack = {
            scope.launch {
                viewSongArtistSheetState.hide()
            }.invokeOnCompletion {
                onEvent(RootDrawerUiEvent.OnViewSongArtistsCancel)
            }
        }
    )

    if (drawerUiState.addToPlaylistUiState.isOpen ||
        drawerUiState.createPlaylistUiState.isOpen ||
        drawerUiState.viewUiState.isOpen ||
        drawerUiState.exploreArtistUiState.isOpen ||
        drawerUiState.newArtisUiState.isOpen ||
        drawerUiState.newAlbumUiState.isOpen ||
        playerUiState.isPlayerExtended
    ) BackHandler {
        if (playerUiState.isPlayerExtended) onPlayerEvent(PlayerUiEvent.OnPlayerShrinkClick)
        else if (drawerUiState.createPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
        else if (drawerUiState.addToPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
        else if (drawerUiState.viewUiState.isOpen) onEvent(RootDrawerUiEvent.OnViewCancel)
        else if (drawerUiState.exploreArtistUiState.isOpen) onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
        else if (drawerUiState.newArtisUiState.isOpen) onEvent(RootDrawerUiEvent.NewArtistCancel)
        else if (drawerUiState.newAlbumUiState.isOpen) onEvent(RootDrawerUiEvent.NewAlbumCancel)
    }
}