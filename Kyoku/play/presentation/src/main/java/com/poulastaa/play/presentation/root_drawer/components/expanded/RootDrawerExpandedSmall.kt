package com.poulastaa.play.presentation.root_drawer.components.expanded

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
import com.poulastaa.play.presentation.player.full_player.HorizontalPlayerScreen
import com.poulastaa.play.presentation.player.small_player.SmallCompactPlayer
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
fun RootDrawerExpandedSmall(
    viewSongArtistSheetState: SheetState,
    navController: NavHostController,
    state: RootDrawerUiState,
    onEvent: (RootDrawerUiEvent) -> Unit,
    onPlayerEvent: (PlayerUiEvent) -> Unit,
) {
    var dragScope by remember { mutableFloatStateOf(0f) }
    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row { // don't remove the row
            RootDrawerExpanded(
                viewSongArtistSheetState = viewSongArtistSheetState,
                navController = navController,
                state = state,
                onSaveScreenToggle = {
                    onEvent(RootDrawerUiEvent.SaveScreenToggle(it))
                },
                onEvent = onEvent,
                onPlayerEvent = {},
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.newAlbumUiState.isOpen,
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
            visible = state.newArtisUiState.isOpen,
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
            visible = state.exploreArtistUiState.isOpen,
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

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.viewUiState.isOpen,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            ViewCompactScreen(
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
            visible = state.addToPlaylistUiState.isOpen,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            AddToPlaylistRootScreen(songId = state.addToPlaylistUiState.songId) {
                onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.createPlaylistUiState.isOpen,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            CreatePlaylistRootScreen(
                playlistId = state.createPlaylistUiState.playlistId,
                navigateBack = {
                    onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.player.isPlayerExtended,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInVertically(tween(400)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center) +
                    slideOutVertically(tween(400)) { it }
        ) {
            if (state.player.queue.isNotEmpty()) HorizontalPlayerScreen(
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
                        )

                        onPlayerEvent(PlayerUiEvent.OnPlayerShrinkClick)
                    }
                    else onPlayerEvent(it)
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .align(Alignment.BottomCenter)
                .padding(bottom = MaterialTheme.dimens.small1)
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    onPlayerEvent(PlayerUiEvent.OnPlayerExtendClick)
                }
                .offset {
                    IntOffset(
                        0,
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
                        else dragScope = 0f
                    },
                ),
            visible = state.player.isData &&
                    state.player.queue.isNotEmpty() &&
                    config.screenWidthDp < 980 &&
                    !state.createPlaylistUiState.isOpen &&
                    !state.player.isPlayerExtended,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                    slideInVertically(tween(400)) { it },
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center) +
                    slideOutVertically(tween(400)) { it }
        ) {
            if (state.player.queue.isNotEmpty()) SmallCompactPlayer(
                modifier = Modifier.padding(MaterialTheme.dimens.small1),
                header = state.header,
                height = 95.dp,
                song = state.player.queue[state.player.info.currentPlayingIndex].copy(
                    colors = state.player.queue[state.player.info.currentPlayingIndex].colors.ifEmpty {
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    }
                ),
                info = state.player.info,
                onEvent = onPlayerEvent
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
                onEvent(RootDrawerUiEvent.OnViewCancel)
                onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
            }

            navController.navigate(
                route = DrawerScreen.ViewArtist.route + "/$it"
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

    if (state.addToPlaylistUiState.isOpen ||
        state.viewUiState.isOpen ||
        state.exploreArtistUiState.isOpen ||
        state.newArtisUiState.isOpen ||
        state.newAlbumUiState.isOpen ||
        state.createPlaylistUiState.isOpen ||
        state.player.isPlayerExtended
    ) BackHandler {
        if (state.player.isPlayerExtended) onPlayerEvent(PlayerUiEvent.ClosePlayer)
        else if (state.addToPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.OnAddSongToPlaylistCancel)
        else if (state.viewUiState.isOpen) onEvent(RootDrawerUiEvent.OnViewCancel)
        else if (state.exploreArtistUiState.isOpen) onEvent(RootDrawerUiEvent.OnExploreArtistCancel)
        else if (state.newArtisUiState.isOpen) onEvent(RootDrawerUiEvent.NewArtistCancel)
        else if (state.newAlbumUiState.isOpen) onEvent(RootDrawerUiEvent.NewAlbumCancel)
        else if (state.createPlaylistUiState.isOpen) onEvent(RootDrawerUiEvent.CreatePlaylistCancel)
    }
}
