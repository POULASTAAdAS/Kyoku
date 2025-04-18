package com.poulastaa.add.presentation.playlist.album

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@Composable
internal fun AddSongToAlbumRootScreen(
    viewmodel: AddSongToPlaylistAlbumViewmodel = hiltViewModel(),
    albumId: AlbumId,
    playlistId: PlaylistId,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(albumId != -1L) {
        viewmodel.init(albumId, playlistId)
    }

    val context = LocalContext.current as Activity
    val state by viewmodel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewmodel.uiEvent) { action ->
        when (action) {
            is AddSongToPlaylistAlbumUiEvent.EmitToast -> Toast.makeText(
                context,
                action.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    AddSongToPlaylistAlbumScreen(
        state = state,
        onAction = viewmodel::onAction,
        navigateBack = navigateBack
    )
}