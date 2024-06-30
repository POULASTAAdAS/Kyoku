package com.poulastaa.setup.presentation.get_spotify_playlist.components

import android.graphics.Bitmap
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
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist

@Composable
fun ListContent(
    elevation: CardElevation,
    data: List<UiPlaylist>,
    header: String,
    verticalArrangement: Arrangement.Vertical,
    contentPadding: PaddingValues,
    internalPadding: Dp,
    cardClick: (id: Long) -> Unit,
    storeImageColor: (id: Long, bitmap: Bitmap) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
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
                elevation = elevation,
                header = header,
                playlist = playlist,
                internalPadding = internalPadding,
                storeImageColor = storeImageColor
            )
        }
    }
}