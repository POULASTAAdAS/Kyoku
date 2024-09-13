package com.poulastaa.play.presentation.root_drawer.components.expanded

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.poulastaa.core.presentation.designsystem.dimens
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
import com.poulastaa.play.presentation.root_drawer.toPlayType
import com.poulastaa.play.presentation.view.ViewCompactScreen
import com.poulastaa.play.presentation.view.ViewOtherScreen
import com.poulastaa.play.presentation.view.components.ViewDataType

@Composable
fun RootDrawerExpandedLarge(
    navController: NavHostController,
    state: RootDrawerUiState,
    onEvent: (RootDrawerUiEvent) -> Unit,
) {
    Row { // don't remove the row
        RootDrawerExpanded(
            navController = navController,
            state = state,
            onSaveScreenToggle = {
                onEvent(RootDrawerUiEvent.SaveScreenToggle(it))
            },
            onEvent = onEvent,
        )

        AnimatedVisibility(
            visible = state.addToPlaylistUiState.isOpen,
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
            visible = state.viewUiState.isOpen,
            enter = fadeIn(),
            exit = fadeOut()
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
            visible = state.newAlbumUiState.isOpen,
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
            visible = state.exploreArtistUiState.isOpen,
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
            modifier = Modifier.fillMaxSize(),
            visible = state.createPlaylistUiState.isOpen,
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
