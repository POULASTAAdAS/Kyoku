package com.poulastaa.setup.presentation.get_spotify_playlist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.LinkIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.get_spotify_playlist.SpotifyPlaylistUiEvent
import com.poulastaa.setup.presentation.get_spotify_playlist.SpotifyPlaylistUiState
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiSong
import kotlin.random.Random

@Composable
fun SpotifyCompactScreen(
    state: SpotifyPlaylistUiState,
    paddingValues: PaddingValues,
    onEvent: (SpotifyPlaylistUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val elevation = CardDefaults.cardElevation(
        defaultElevation = 3.dp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .padding(top = MaterialTheme.dimens.medium1),
    ) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth(),
            text = state.link.data,
            onValueChange = { onEvent(SpotifyPlaylistUiEvent.OnLinkChange(it)) },
            label = stringResource(id = R.string.link),
            isErr = state.link.isErr,
            supportingText = state.link.errText.asString(),
            trailingIcon = {
                Icon(
                    imageVector = LinkIcon,
                    contentDescription = null
                )
            },
            onDone = {
                onEvent(SpotifyPlaylistUiEvent.OnLikeSubmit)
                focusManager.clearFocus()
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AddButton(isMakingApiCall = state.isMakingApiCall) {
                onEvent(SpotifyPlaylistUiEvent.OnLikeSubmit)
                focusManager.clearFocus()
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = MaterialTheme.dimens.medium1)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary.copy(.5f),
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            ListContent(
                elevation = elevation,
                data = state.playlists,
                header = state.authHeader,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
                contentPadding = PaddingValues(MaterialTheme.dimens.small3),
                internalPadding = MaterialTheme.dimens.small3,
                cardClick = {
                    onEvent(SpotifyPlaylistUiEvent.OnPlaylistClick(it))
                },
                storeImageColor = { id, bitmap ->
                    onEvent(SpotifyPlaylistUiEvent.StoreImageColor(id, bitmap))
                }
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    val playlists = (1..6).map { id ->
        (1..2).map {
            UiSong(
                id = it.toLong(),
                title = "Song Title $it",
                artist = "Artist1 , Artist 2",
                coverImage = ""
            )
        }.let {
            UiPlaylist(
                id = id.toLong(),
                name = "Playlist $id",
                listOfUiSong = it,
                isExpanded = Random.nextBoolean()
            )
        }
    }

    AppThem {
        SpotifyCompactScreen(
            state = SpotifyPlaylistUiState(
                playlists = playlists
            ),
            paddingValues = PaddingValues()
        ) {

        }
    }
}