package com.poulastaa.play.presentation.create_playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.UiSong
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistData
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistUiEvent

@Composable
fun CommonHorizontalPager(
    modifier: Modifier = Modifier,
    state: CreatePlaylistData,
    header: String,
    onEvent: (CreatePlaylistUiEvent) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.medium1),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimens.small3),
            contentPadding = PaddingValues(vertical = MaterialTheme.dimens.small3)
        ) {
            items(state.list) { song ->
                CreatePlaylistSongCard(
                    header = header,
                    song = song,
                    modifier = Modifier
                        .clickable {
                            onEvent(
                                CreatePlaylistUiEvent.OnSongClick(
                                    type = state.type,
                                    songId = song.id
                                )
                            )
                        }
                        .padding(MaterialTheme.dimens.small1)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            CommonHorizontalPager(
                header = "",
                state = CreatePlaylistData(
                    type = CreatePlaylistType.RECENT_HISTORY,
                    list = (1..10).map {
                        UiSong(
                            id = it.toLong(),
                            title = "That Cool title $it",
                            artist = "That Cool Artist $it",
                            coverImage = "",
                        )
                    }
                )
            ) { }
        }
    }
}