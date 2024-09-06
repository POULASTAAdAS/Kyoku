package com.poulastaa.setup.presentation.get_spotify_playlist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.LinkIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.get_spotify_playlist.SpotifyPlaylistUiEvent
import com.poulastaa.setup.presentation.get_spotify_playlist.SpotifyPlaylistUiState
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist
import com.poulastaa.core.presentation.ui.model.UiSong
import kotlin.random.Random


@Composable
fun SpotifyMediumScreen(
    paddingValues: PaddingValues,
    state: SpotifyPlaylistUiState,
    onEvent: (SpotifyPlaylistUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val elevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimens.large2)
            .padding(top = MaterialTheme.dimens.large1),
    ) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.large2 + MaterialTheme.dimens.large2),
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

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = Modifier,
                onClick = {
                    onEvent(SpotifyPlaylistUiEvent.OnLikeSubmit)
                    focusManager.clearFocus()
                },
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(.2f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.add),
                        modifier = Modifier
                            .alpha(if (state.isMakingApiCall) 0f else 1f)
                            .padding(vertical = MaterialTheme.dimens.small3),
                        textAlign = TextAlign.Center,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )

                    CircularProgressIndicator(
                        modifier = Modifier
                            .alpha(if (state.isMakingApiCall) 1f else 0f),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

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
            SpotifyListContent(
                elevation = elevation,
                data = state.playlists,
                header = state.authHeader,
                itemDefaultHeight = 150.dp,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
                contentPadding = PaddingValues(MaterialTheme.dimens.small3),
                internalPadding = MaterialTheme.dimens.medium2,
                cardClick = {
                    onEvent(SpotifyPlaylistUiEvent.OnPlaylistClick(it))
                }
            )
        }
    }
}

@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480"
)
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
        SpotifyMediumScreen(
            state = SpotifyPlaylistUiState(
                playlists = playlists
            ),
            paddingValues = PaddingValues(),
        ) {

        }
    }
}