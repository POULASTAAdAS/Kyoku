package com.poulastaa.play.presentation.root_drawer.components.expanded

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumOtherScreen
import com.poulastaa.play.presentation.add_new_album.AddNewAlbumRootScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistOtherScreen
import com.poulastaa.play.presentation.add_new_artist.AddNewArtistRootScreen
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistRootScreen
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistRootScreen
import com.poulastaa.play.presentation.explore_artist.ExploreArtistOtherScreen
import com.poulastaa.play.presentation.explore_artist.ExploreArtistRootScreen
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.full_player.VerticalPlayerScreen
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiState
import com.poulastaa.play.presentation.root_drawer.toPlayType
import com.poulastaa.play.presentation.song_artist.SongArtistsBottomSheet
import com.poulastaa.play.presentation.view.ViewCompactScreen
import com.poulastaa.play.presentation.view.ViewOtherScreen
import com.poulastaa.play.presentation.view.components.ViewDataType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootDrawerExpandedLarge(
    viewSongArtistSheetState: SheetState,
    navController: NavHostController,
    state: RootDrawerUiState,
    onEvent: (RootDrawerUiEvent) -> Unit,
    onPlayerEvent: (PlayerUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Row { // don't remove the row
        RootDrawerExpanded(
            viewSongArtistSheetState = viewSongArtistSheetState,
            navController = navController,
            state = state,
            onSaveScreenToggle = {
                onEvent(RootDrawerUiEvent.SaveScreenToggle(it))
            },
            onEvent = onEvent,
            onPlayerEvent = onPlayerEvent,
        )

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.addToPlaylistUiState.isOpen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
        ) {
            AddToPlaylistRootScreen(
                songId = state.addToPlaylistUiState.songId,
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2)
            ) {
                onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
            }
        }


        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.newAlbumUiState.isOpen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
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
            modifier = Modifier.fillMaxSize(),
            visible = state.exploreArtistUiState.isOpen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
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
            visible = state.newArtisUiState.isOpen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
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
            visible = state.createPlaylistUiState.isOpen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
        ) {
            CreatePlaylistRootScreen(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                playlistId = state.createPlaylistUiState.playlistId,
                navigateBack = {
                    onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.viewUiState.isOpen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
        ) {
            ViewCompactScreen(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                id = state.viewUiState.songId,
                type = state.viewUiState.type,
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

                        is ViewOtherScreen.PlayOperation.PlayAll -> onEvent(
                            RootDrawerUiEvent.PlayOperation.PlaySaved(
                                id = event.id,
                                type = event.type.toPlayType()
                            )
                        )

                        is ViewOtherScreen.PlayOperation.Shuffle -> onEvent(
                            RootDrawerUiEvent.PlayOperation.ShuffleSaved(
                                id = event.id,
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
            visible = state.player.isPlayerExtended,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInHorizontally(tween(300)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterEnd) +
                    slideOutHorizontally { it }
        ) {
            if (state.player.queue.isNotEmpty()) VerticalPlayerScreen(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                header = state.header,
                song = state.player.queue[state.player.info.currentPlayingIndex].copy(
                    colors = state.player.queue[state.player.info.currentPlayingIndex].colors.ifEmpty {
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    }
                ),
                info = state.player.info,
                queue = state.player.queue,
                onEvent = {
                    if (it is PlayerUiEvent.OnArtistClick) {
                        navController.navigate(
                            route = DrawerScreen.ViewArtist.route + "/${it.artistId}"
                        ) {
                            popUpTo(DrawerScreen.ViewArtist.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else onPlayerEvent(it)
                }
            )
        }
    }

    if (viewSongArtistSheetState.isVisible) SongArtistsBottomSheet(
        songId = state.viewSongArtistSongId,
        sheetState = viewSongArtistSheetState,
        navigateToArtistScreen = {
            scope.launch {
                viewSongArtistSheetState.hide()
            }.invokeOnCompletion {
                onEvent(RootDrawerUiEvent.OnViewSongArtistsCancel)
            }

            navController.navigate(
                route = DrawerScreen.ViewArtist.route + "/$it"
            ) {
                popUpTo(DrawerScreen.ViewArtist.route) { inclusive = true }
                launchSingleTop = true
            }
        },
        navigateBack = {
            scope.launch {
                viewSongArtistSheetState.hide()
            }.invokeOnCompletion {
                onEvent(RootDrawerUiEvent.OnViewSongArtistsCancel)
            }
        }
    )

    if (state.addToPlaylistUiState.isOpen ||
        state.createPlaylistUiState.isOpen ||
        state.viewUiState.isOpen ||
        state.exploreArtistUiState.isOpen ||
        state.newArtisUiState.isOpen ||
        state.newAlbumUiState.isOpen ||
        state.player.isPlayerExtended
    ) BackHandler {
        if (state.player.isPlayerExtended) onPlayerEvent(PlayerUiEvent.OnPlayerShrinkClick)
        else if (state.createPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
        else if (state.addToPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
        else if (state.viewUiState.isOpen) onEvent(RootDrawerUiEvent.OnViewCancel)
        else if (state.exploreArtistUiState.isOpen) onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
        else if (state.newArtisUiState.isOpen) onEvent(RootDrawerUiEvent.NewArtistCancel)
        else if (state.newAlbumUiState.isOpen) onEvent(RootDrawerUiEvent.NewAlbumCancel)
    }
}
