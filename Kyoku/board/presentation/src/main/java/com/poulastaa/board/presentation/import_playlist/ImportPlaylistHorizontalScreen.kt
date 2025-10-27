package com.poulastaa.board.presentation.import_playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poulastaa.board.presentation.R
import com.poulastaa.board.presentation.import_playlist.component.PlaylistCard
import com.poulastaa.board.presentation.import_playlist.component.SkipButton
import com.poulastaa.core.presentation.ConformButton
import com.poulastaa.core.presentation.designsystem.UiPrevPlaylistSong
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewCompactLandscape
import com.poulastaa.core.presentation.ui.PreviewLandscape
import com.poulastaa.core.presentation.ui.dimens
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImportPlaylistHorizontalScreen(
    modifier: Modifier = Modifier,
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            SkipButton(modifier = Modifier.fillMaxWidth(.2f)) {
                onAction(ImportPlaylistUiAction.OnSkipClick)
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.dimens.medium1)
                .padding(paddingValues)
                .then(modifier)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.4f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.import_your_spotify_playlist),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }

                Spacer(Modifier.weight(1f))

                ImportPlaylistTextField(
                    Modifier.fillMaxWidth(),
                    link = state.link,
                    onAction = onAction
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                ConformButton(
                    modifier = Modifier.fillMaxWidth(.4f),
                    isLoading = state.isMakingApiCall,
                    heading = stringResource(R.string.import_button),
                    onClick = {
                        onAction(ImportPlaylistUiAction.OnImportClick)
                    }
                )

                Spacer(Modifier.weight(1f))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.dimens.medium1)
                        .border(
                            width = 1.3.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = MaterialTheme.shapes.small
                        ),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
                ) {
                    items(state.data) { playlist ->
                        PlaylistCard(playlist, onAction)

                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                    }
                }
            }
        }
    }
}


@PreviewLandscape
@PreviewCompactLandscape
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