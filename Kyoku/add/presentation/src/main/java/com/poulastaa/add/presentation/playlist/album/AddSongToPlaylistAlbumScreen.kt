package com.poulastaa.add.presentation.playlist.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.add.presentation.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiItem
import com.poulastaa.add.presentation.playlist.AddToPlaylistItemUiType
import com.poulastaa.add.presentation.playlist.components.CreatePlaylistItemCard
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.core.presentation.ui.components.AppErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistAlbumScreen(
    state: AddSongToPlaylistAlbumUiState,
    onAction: (AddSongToPlaylistAlbumUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            if (state.loadingSate is LoadingType.Loading) AddSongToPlaylistLoadingTopBar(
                navigateBack = navigateBack
            )
            else if (state.loadingSate is LoadingType.Content) {
                AppBasicTopBar(
                    title = state.album,
                    navigateBack = navigateBack
                )
            }
        }
    ) {
        when (state.loadingSate) {
            LoadingType.Loading -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(10) {
                    LoadingSongCard(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                }
            }

            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .padding(it),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                items(state.data) { item ->
                    CreatePlaylistItemCard(
                        item = item,
                        onAction = {
                            onAction(AddSongToPlaylistAlbumUiAction.OnItemClick(item.id))
                        },
                        haptic = haptic
                    )
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingSate,
                navigateBack = navigateBack
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistAlbumScreen(
                state = AddSongToPlaylistAlbumUiState(
                    loadingSate = LoadingType.Loading,
                    album = "That Cool Album",
                    data = (1..10).map {
                        AddSongToPlaylistUiItem(
                            id = it.toLong(),
                            title = "That Cool Song",
                            artist = "That Cool Artist",
                            type = AddToPlaylistItemUiType.SONG
                        )
                    }
                ),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}