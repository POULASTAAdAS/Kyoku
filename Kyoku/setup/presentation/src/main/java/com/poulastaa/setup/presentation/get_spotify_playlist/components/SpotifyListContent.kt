package com.poulastaa.setup.presentation.get_spotify_playlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist

@Composable
fun SpotifyListContent(
    elevation: CardElevation,
    itemDefaultHeight: Dp = 100.dp,
    header: String,
    data: List<UiPlaylist>,
    verticalArrangement: Arrangement.Vertical,
    contentPadding: PaddingValues,
    internalPadding: Dp,
    cardClick: (id: Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = verticalArrangement,
        contentPadding = contentPadding
    ) {
        items(
            items = data,
            key = {
                it.id
            }
        ) { playlist ->
            PlaylistCard(
                modifier = Modifier.clickable(
                    onClick = {
                        cardClick(playlist.id)
                    },
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ),
                itemDefaultHeight = itemDefaultHeight,
                header = header,
                elevation = elevation,
                playlist = playlist,
                internalPadding = internalPadding,
            )
        }
    }
}