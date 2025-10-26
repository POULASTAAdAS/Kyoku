package com.poulastaa.board.presentation.import_playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poulastaa.core.presentation.designsystem.UiPrevPlaylistSong
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewLandscape
import com.poulastaa.core.presentation.ui.dimens
import kotlin.random.Random

@Composable
internal fun ImportPlaylistHorizontalScreen(
    modifier: Modifier = Modifier,
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.medium1)
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.35f)
                .fillMaxHeight(),
        ) {

        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
        }
    }
}


@PreviewLandscape
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        ImportPlaylistHorizontalScreen(
            state = ImportPlaylistUiState(
                data = (1..3).map { playlistId ->
                    UiPreviewPlaylist(
                        id = playlistId.toLong(),
                        title = "Playlist $playlistId",
                        songs = (1..5).map { songId ->
                            UiPrevPlaylistSong(
                                id = songId.toLong(),
                                title = "Song $songId",
                                artist = "Artist $songId",
                            )
                        },
                        isExpanded = Random.nextBoolean()
                    )
                }
            )
        ) { }
    }
}