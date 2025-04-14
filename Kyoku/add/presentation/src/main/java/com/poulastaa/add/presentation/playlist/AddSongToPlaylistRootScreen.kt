package com.poulastaa.add.presentation.playlist

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddSongToPlaylistRootScreen(
    playlistId: PlaylistId,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<AddSongToPlaylistViewmodel>()

    LaunchedEffect(playlistId) {
        viewmodel.init(playlistId)
    }

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)

    val state by viewmodel.state.collectAsStateWithLifecycle()
    val searchData = viewmodel.searchData.collectAsLazyPagingItems()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is AddSongToPlaylistUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            AddSongToPlaylistCompactScreen(
                state = state,
                searchData = searchData,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            AddSongToPlaylistCompactScreen(
                state = state,
                searchData = searchData,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            AddSongToPlaylistExpandedScreen(
                state = state,
                searchData = searchData,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        }
    )
}


internal val PREV_DATA = flowOf(
    PagingData.from(
        ((1..5).map {
            AddSongToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Song",
                type = AddToPlaylistItemUiType.SONG
            )
        } + (1..5).map {
            AddSongToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Artist",
                type = AddToPlaylistItemUiType.ARTIST
            )
        } + (1..5).map {
            AddSongToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Playlist",
                type = AddToPlaylistItemUiType.PLAYLIST
            )
        } + (1..5).map {
            AddSongToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Artist",
                type = AddToPlaylistItemUiType.ALBUM
            )
        }).shuffled()
    )
)