package com.poulastaa.setup.presentation.get_spotify_playlist.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist

@Composable
fun ListContent(
    elevation: CardElevation,
    data: List<UiPlaylist>,
    verticalArrangement: Arrangement.Vertical,
    contentPadding: PaddingValues,
    internalPadding: Dp,
    storeLoadedBitmapInLocalDatabase: (id: Long, bitmap: Bitmap) -> Unit,
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
                elevation = elevation,
                playlist = playlist,
                internalPadding = internalPadding,
                storeLoadedBitmapInLocalDatabase = storeLoadedBitmapInLocalDatabase
            )
        }
    }
}