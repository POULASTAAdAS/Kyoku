package com.poulastaa.add.presentation.playlist.artist

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistArtistRootScreen(
    artistId: ArtistId,
    playlistId: PlaylistId,
    viewmodel: AddSongToPlaylistArtistViewmodel = hiltViewModel<AddSongToPlaylistArtistViewmodel>(),
    navigate: (albumId: AlbumId) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current as Activity
    LaunchedEffect(artistId) {
        viewmodel.init(playlistId, artistId)
    }

    val state by viewmodel.state.collectAsStateWithLifecycle()
    val searchData = viewmodel.data.collectAsLazyPagingItems()
    val focusManager = LocalFocusManager.current

    ObserveAsEvent(viewmodel.uiEvent) { action ->
        when (action) {
            is AddSongTOPlaylistArtistUiEvent.EmitToast -> Toast.makeText(
                context,
                action.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is AddSongTOPlaylistArtistUiEvent.NavigateToAlbum -> {
                focusManager.clearFocus()
                navigate(action.albumId)
            }
        }
    }

    AddSongToPlaylistArtistScreen(
        focusManager = focusManager,
        scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        state = state,
        searchData = searchData,
        onAction = viewmodel::onAction,
        navigateBack = navigateBack
    )
}