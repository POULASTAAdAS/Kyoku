package com.poulastaa.play.presentation.create_playlist.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CreatePlaylistAlbumRootScreen(
    modifier: Modifier = Modifier,
    albumId: Long,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(it)
        ) {

        }
    }
}